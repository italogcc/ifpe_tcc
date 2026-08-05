package com.sees.projeto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import com.sees.projeto.model.User;
import com.sees.projeto.service.UserService;

@Controller
public class AuthController
{

    @Autowired
    private UserService userService;

    // ==================== REGISTRO ====================
    @GetMapping("/registrar")
    public String mostrarRegistro() {
        return "registro-tipo";
    }

    @GetMapping("/registrar/formulario")
    public String mostrarFormulario(@RequestParam String tipo, Model model) {
        model.addAttribute("tipo", tipo);
        return "registro-form";
    }

    @PostMapping("/registrar")
    public String registrar(
            @RequestParam String tipo,
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sobrenome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String rg,
            @RequestParam(required = false) String dataNascimento,
            @RequestParam(required = false) String razaoSocial,
            @RequestParam(required = false) String nomeFantasia,
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) String inscricaoEstadual,
            @RequestParam(required = false) String logradouro,
            @RequestParam(required = false) String logNumero,
            @RequestParam(required = false) String complemento,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String cep,
            @RequestParam(required = false) String telefone1,
            @RequestParam(required = false) String telefone2,
            RedirectAttributes redirectAttributes) {

        // TODO: adaptar o UserService para receber todos esses campos.
        // Exemplo usando os dados básicos enquanto o service não é evoluído:
        String nomePrincipal = tipo.equals("PJ") || tipo.equals("F")
                ? razaoSocial
                : nome;

        boolean sucesso = userService.registrar(nomePrincipal, email, senha);

        if (!sucesso) {
            redirectAttributes.addFlashAttribute("erro", "Email já cadastrado!");
            return "redirect:/registrar/formulario?tipo=" + tipo;
        }

        redirectAttributes.addFlashAttribute("sucesso", "Registro realizado! Faça o login.");
        return "redirect:/login";
    }

    // ==================== LOGIN ====================
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String senha,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = userService.autenticar(email, senha);

        if (user == null) {
            redirectAttributes.addFlashAttribute("erro", "Email ou senha inválidos!");
            return "redirect:/login";
        }

        session.setAttribute("usuarioLogado", user);
        return "redirect:/painel";
    }

    // ==================== LOGOUT ====================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutPost(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
}
