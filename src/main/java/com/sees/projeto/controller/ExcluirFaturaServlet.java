package com.sees.projeto.controller;

import com.sees.projeto.model.Fatura;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@WebServlet("/excluir-fatura")
public class ExcluirFaturaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int mes = Integer.parseInt(request.getParameter("mes"));
        int ano = Integer.parseInt(request.getParameter("ano"));

        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<Fatura> faturas = (List<Fatura>) session.getAttribute("faturas");

        if (faturas != null) {
            Iterator<Fatura> it = faturas.iterator();
            while (it.hasNext()) {
                Fatura f = it.next();
                if (f.getMesReferencia() == mes && f.getAnoReferencia() == ano) {
                    it.remove();
                    break;
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/painel");
    }
}
