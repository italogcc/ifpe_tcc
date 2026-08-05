package com.sees.projeto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import com.sees.projeto.model.Fatura;

@Service
public class FaturaService {

    // Armazenamento em memória (email → lista de faturas)
    private final Map<String, List<Fatura>> faturasPorUsuario = new ConcurrentHashMap<>();

    /**
     * Adiciona uma fatura para o usuário.
     * @return true se adicionada, false se já atingiu o limite de 3 faturas.
     */
    public boolean adicionarFatura(String email, Fatura fatura) {
        List<Fatura> faturas = faturasPorUsuario.computeIfAbsent(email, k -> new ArrayList<>());

        if (faturas.size() >= 3) {
            return false; // limite de 3 faturas
        }

        faturas.add(fatura);
        return true;
    }

    /**
     * Retorna as faturas do usuário.
     */
    public List<Fatura> listarFaturas(String email) {
        return faturasPorUsuario.getOrDefault(email, new ArrayList<>());
    }

    /**
     * Retorna a quantidade de faturas cadastradas pelo usuário.
     */
    public int quantidadeFaturas(String email) {
        List<Fatura> faturas = faturasPorUsuario.get(email);
        return faturas != null ? faturas.size() : 0;
    }

    /**
     * Calcula a média de consumo em kWh.
     */
    public float mediaConsumokWh(String email) {
        List<Fatura> faturas = faturasPorUsuario.get(email);
        if (faturas == null || faturas.isEmpty()) return 0;

        float soma = 0;
        for (Fatura f : faturas) {
            soma += f.getConsumokWh();
        }
        return soma / faturas.size();
    }

    /**
     * Calcula a média do valor da fatura.
     */
    public float mediaValorFatura(String email) {
        List<Fatura> faturas = faturasPorUsuario.get(email);
        if (faturas == null || faturas.isEmpty()) return 0;

        float soma = 0;
        for (Fatura f : faturas) {
            soma += f.getValorFatura();
        }
        return soma / faturas.size();
    }
}
