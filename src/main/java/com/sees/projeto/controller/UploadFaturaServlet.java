package com.sees.projeto.controller;

import com.sees.projeto.service.ParserFaturaNeoenergiaPE;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.Map;

@WebServlet("/adicionar-fatura/upload")
@MultipartConfig(
    fileSizeThreshold   = 1024 * 1024,       // 1 MB  — buffer em memória
    maxFileSize         = 10 * 1024 * 1024,   // 10 MB — tamanho máximo por arquivo
    maxRequestSize      = 15 * 1024 * 1024    // 15 MB — tamanho máximo da requisição
)
public class UploadFaturaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Part filePart = request.getPart("pdfFile");

        if (filePart == null || filePart.getSize() == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"Nenhum arquivo PDF enviado.\"}");
            return;
        }

        String fileName = filePart.getSubmittedFileName();
        if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"erro\": \"O arquivo deve ser um PDF.\"}");
            return;
        }

        try (var pdfStream = filePart.getInputStream()) {
            ParserFaturaNeoenergiaPE parser = new ParserFaturaNeoenergiaPE();
            Map<String, Object> dados = parser.parse(pdfStream);

            // Constrói JSON manualmente com os 4 campos principais
            String json = "{"
                    + "\"mesReferencia\": " + dados.get("mesReferencia") + ","
                    + "\"anoReferencia\": " + dados.get("anoReferencia") + ","
                    + "\"consumokWh\": "    + dados.get("consumokWh")    + ","
                    + "\"valorFatura\": "   + dados.get("valorFatura")
                    + "}";

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(json);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(
                "{\"erro\": \"Falha ao processar o PDF: " + escaparJson(e.getMessage()) + "\"}"
            );
        }
    }

    /** Escapa caracteres especiais para uso em string JSON. */
    private String escaparJson(String texto) {
        if (texto == null) return "";
        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
