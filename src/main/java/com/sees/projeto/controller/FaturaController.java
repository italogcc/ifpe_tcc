package com.sees.projeto.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import com.sees.projeto.dto.DadosFaturaDTO;
import com.sees.projeto.model.Fatura;
import com.sees.projeto.model.User;
import com.sees.projeto.service.FaturaService;
import com.sees.projeto.service.ParserFaturaService;

@Controller
public class FaturaController {

    @Autowired
    private FaturaService faturaService;

    @Autowired
    private ParserFaturaService parserFaturaService;

    @Value("${app.upload-dir}")
    private String uploadDir;

    // ==================== PAINEL ====================

    @GetMapping("/painel")
    public String painel(HttpSession session, Model model) {
        User user = (User) session.getAttribute("usuarioLogado");
        if (user == null) return "redirect:/login";

        int qtdFaturas = faturaService.quantidadeFaturas(user.getEmail());

        model.addAttribute("usuario", user);
        model.addAttribute("qtdFaturas", qtdFaturas);
        model.addAttribute("faturasCompletas", qtdFaturas >= 3);
        model.addAttribute("faturas", faturaService.listarFaturas(user.getEmail())); // ← sempre disponível

        if (qtdFaturas >= 3) {
            model.addAttribute("mediaConsumo", faturaService.mediaConsumokWh(user.getEmail()));
            model.addAttribute("mediaValor", faturaService.mediaValorFatura(user.getEmail()));
        }

        return "painel";
    }

    // ==================== ADICIONAR FATURA (FORMULÁRIO) ====================

    @GetMapping("/adicionar-fatura")
    public String mostrarFormulario(HttpSession session) {
        User user = (User) session.getAttribute("usuarioLogado");
        if (user == null) return "redirect:/login";
        return "adicionar-fatura";
    }

    // ==================== ADICIONAR FATURA (MANUAL) ====================
    @PostMapping("/adicionar-fatura/manual")
    public String adicionarManual(
            @RequestParam int mes,
            @RequestParam int ano,
            @RequestParam float valor,
            @RequestParam float consumo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("usuarioLogado");
        if (user == null) return "redirect:/login";

        Fatura fatura = new Fatura(mes, ano, valor, consumo);
        boolean ok = faturaService.adicionarFatura(user.getEmail(), fatura);

        if (!ok) {
            redirectAttributes.addFlashAttribute("erro",
                    "Você já cadastrou 3 faturas! Exclua os dados para recomeçar.");
        } else {
            redirectAttributes.addFlashAttribute("sucesso", "Fatura cadastrada com sucesso!");
        }

        return "redirect:/painel";
    }

    // ==================== ADICIONAR FATURA (PDF) ====================

    @PostMapping("/adicionar-fatura/pdf")
    public String adicionarPorPdf(
            @RequestParam("arquivo") MultipartFile arquivo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("usuarioLogado");
        if (user == null) return "redirect:/login";

        if (arquivo.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Selecione um arquivo PDF!");
            return "redirect:/adicionar-fatura";
        }

        try {
            // Cria diretório de upload se não existir
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Salva o arquivo temporariamente
            String nomeArquivo = System.currentTimeMillis() + "_" + arquivo.getOriginalFilename();
            Path caminhoCompleto = uploadPath.resolve(nomeArquivo);
            arquivo.transferTo(caminhoCompleto.toFile());

            // Usa o parser para extrair os dados
            DadosFaturaDTO dados = parserFaturaService.extrairDados(caminhoCompleto.toString());

            // Remove o arquivo temporário após o parsing
            Files.deleteIfExists(caminhoCompleto);

            // Valida se os dados foram extraídos
            if (dados.getMesReferencia() == 0 || dados.getAnoReferencia() == 0) {
                redirectAttributes.addFlashAttribute("erro",
                        "Não foi possível extrair os dados do PDF. Verifique se é uma fatura válida.");
                return "redirect:/adicionar-fatura";
            }

            Fatura fatura = new Fatura(
                    dados.getMesReferencia(),
                    dados.getAnoReferencia(),
                    dados.getValorFatura(),
                    dados.getConsumokWh()
            );

            boolean ok = faturaService.adicionarFatura(user.getEmail(), fatura);

            if (!ok) {
                redirectAttributes.addFlashAttribute("erro",
                        "Você já cadastrou 3 faturas!");
            } else {
                redirectAttributes.addFlashAttribute("sucesso",
                        "Fatura extraída do PDF com sucesso!");
            }

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao processar o PDF: " + e.getMessage());
        }

        return "redirect:/painel";
    }
}

/*
    @GetMapping("/editar-fatura")
    public String mostrarEdicao(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            HttpSession session,
            Model model) {
        // Se mes e ano vierem, carrega aquela fatura específica
        // Se não vierem, lista as faturas para o usuário escolher
    }

*/


    // ==================== AJUDA ====================
    //@GetMapping("/ajuda")
    //public String ajuda() {
    //    return "ajuda";
    //}
