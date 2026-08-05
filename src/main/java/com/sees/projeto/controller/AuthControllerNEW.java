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
public class AuthControllerNEW {

/*
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

    // ==================== AJUSTES DO USUÁRIO ====================

    @GetMapping("/ajustes-usuario")
    public String mostrarAjustes(HttpSession session, Model model) {
        User usuario = (User) session.getAttribute("usuarioLogado");
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        // Recarrega do banco para ter dados atualizados
        User usuarioAtualizado = userService.buscarPorId(usuario.getId());
        model.addAttribute("usuario", usuarioAtualizado);
        
        return "ajustes-usuario";
    }

    @PostMapping("/ajustes-usuario")
    public String atualizarAjustes(
            @RequestParam String email,
            @RequestParam(required = false) String senhaNova,
            @RequestParam(required = false) String senhaConfirmacao,
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
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User usuario = (User) session.getAttribute("usuarioLogado");
        
        if (usuario == null) {
            return "redirect:/login";
        }

        // Recarrega do banco
        User usuarioAtualizado = userService.buscarPorId(usuario.getId());

        // Valida confirmação de senha
        if (senhaNova != null && !senhaNova.isBlank()) {
            if (!senhaNova.equals(senhaConfirmacao)) {
                redirectAttributes.addFlashAttribute("erro", "As senhas não coincidem.");
                return "redirect:/ajustes-usuario";
            }
            usuarioAtualizado.setSenha(senhaNova);
        }

        // Atualiza dados de acesso
        usuarioAtualizado.setEmail(email);

        // Pessoa Física
        if ("PF".equals(usuarioAtualizado.getTipo()) || "A".equals(usuarioAtualizado.getTipo())) {
            usuarioAtualizado.setNome(nome);
            usuarioAtualizado.setSobrenome(sobrenome);
            usuarioAtualizado.setCpf(cpf);
            usuarioAtualizado.setRg(rg);
            usuarioAtualizado.setDataNascimento(dataNascimento);
        }

        // Pessoa Jurídica
        if ("PJ".equals(usuarioAtualizado.getTipo()) || "F".equals(usuarioAtualizado.getTipo())) {
            usuarioAtualizado.setRazaoSocial(razaoSocial);
            usuarioAtualizado.setNomeFantasia(nomeFantasia);
            usuarioAtualizado.setCnpj(cnpj);
            usuarioAtualizado.setInscricaoEstadual(inscricaoEstadual);
        }

        // Endereço e contato
        usuarioAtualizado.setLogradouro(logradouro);
        usuarioAtualizado.setLogNumero(logNumero);
        usuarioAtualizado.setComplemento(complemento);
        usuarioAtualizado.setCidade(cidade);
        usuarioAtualizado.setEstado(estado);
        usuarioAtualizado.setCep(cep);
        usuarioAtualizado.setTelefone1(telefone1);
        usuarioAtualizado.setTelefone2(telefone2);

        // Persiste no banco
        userService.atualizar(usuarioAtualizado);

        // Atualiza sessão
        session.setAttribute("usuarioLogado", usuarioAtualizado);

        redirectAttributes.addFlashAttribute("sucesso", "Alterações salvas com sucesso.");
        return "redirect:/ajustes-usuario";
    }
*/
}
