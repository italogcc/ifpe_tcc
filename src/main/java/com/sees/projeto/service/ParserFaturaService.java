package com.sees.projeto.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import com.sees.projeto.dto.DadosFaturaDTO;

@Service
public class ParserFaturaService {

    /**
     * Extrai os dados da fatura Neoenergia PE a partir de um arquivo PDF.
     * Retorna apenas: mês, ano, valor da fatura e consumo em kWh.
     */
    public DadosFaturaDTO extrairDados(String caminhoPdf) throws IOException {
        File fatura = new File(caminhoPdf);

        if (!fatura.exists()) {
            throw new IOException("Arquivo PDF não encontrado: " + caminhoPdf);
        }

        try (PDDocument document = Loader.loadPDF(fatura)) {
            PDFTextStripper stripper = new PDFTextStripper();

            List<String> linhas = stripper.getText(document)
                    .lines()
                    .map(String::trim)
                    .filter(linha -> !linha.isEmpty())
                    .toList();

            // Dados que nos interessam
            int mesReferencia = 0;
            int anoReferencia = 0;
            float consumokWh = 0;
            float valorFatura = 0;

            for (int i = 0; i < linhas.size(); i++) {
                String linha = linhas.get(i);

                // Referência da fatura (MÊS/ANO)
                if (linha.equalsIgnoreCase("REF:MÊS/ANO")) {
                    if (i + 1 < linhas.size()) {
                        String referencia = linhas.get(i + 1).trim();
                        String[] partes = referencia.split("/");
                        mesReferencia = Integer.parseInt(partes[0]);
                        anoReferencia = Integer.parseInt(partes[1]);
                    }
                    continue;
                }

                // Consumo em kWh
                if (linha.contains("Consumo-TUSD kWh")) {
                    String[] partes = linha.trim().split("\\s+");
                    consumokWh = Float.parseFloat(partes[2].replace(",", "."));
                    continue;
                }

                // Valor da fatura
                if (linha.equalsIgnoreCase("TOTAL A PAGAR R$")) {
                    if (i + 1 < linhas.size()) {
                        String valor = linhas.get(i + 1)
                                .replace("R$", "")
                                .replace(".", "")
                                .replace(",", ".")
                                .trim();
                        valorFatura = Float.parseFloat(valor);
                    }
                    continue;
                }
            }

            return new DadosFaturaDTO(mesReferencia, anoReferencia, valorFatura, consumokWh);
        }
    }
}
