package com.sees.projeto.controller;

import com.sees.projeto.model.Fatura;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/editar-fatura")
public class EditarFaturaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mesStr = request.getParameter("mes");
        String anoStr = request.getParameter("ano");

        if (mesStr == null || anoStr == null) {
            response.sendRedirect(request.getContextPath() + "/painel");
            return;
        }

        int mes = Integer.parseInt(mesStr);
        int ano = Integer.parseInt(anoStr);

        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<Fatura> faturas = (List<Fatura>) session.getAttribute("faturas");

        Fatura faturaEditada = null;
        if (faturas != null) {
            for (Fatura f : faturas) {
                if (f.getMesReferencia() == mes && f.getAnoReferencia() == ano) {
                    faturaEditada = f;
                    break;
                }
            }
        }

        if (faturaEditada == null) {
            response.sendRedirect(request.getContextPath() + "/painel");
            return;
        }

        request.setAttribute("fatura", faturaEditada);
        request.getRequestDispatcher("/adicionar-fatura.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int mes       = Integer.parseInt(request.getParameter("mes"));
        int ano       = Integer.parseInt(request.getParameter("ano"));
        float consumo = Float.parseFloat(request.getParameter("consumo"));
        float valor   = Float.parseFloat(request.getParameter("valor"));

        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<Fatura> faturas = (List<Fatura>) session.getAttribute("faturas");

        if (faturas != null) {
            for (Fatura f : faturas) {
                if (f.getMesReferencia() == mes && f.getAnoReferencia() == ano) {
                    f.setConsumokWh(consumo);
                    f.setValorFatura(valor);
                    break;
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/painel");
    }
}
