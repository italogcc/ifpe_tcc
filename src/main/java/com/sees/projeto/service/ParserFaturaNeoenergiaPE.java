package com.sees.projeto.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Parser especializado para faturas de energia da Neoenergia Pernambuco.
 * Extrai dados relevantes de um PDF e os retorna em um Map estruturado.
 */
public class ParserFaturaNeoenergiaPE {

    /**
     * Processa um PDF de fatura Neoenergia PE e extrai os dados.
     *
     * @param pdfStream InputStream do arquivo PDF
     * @return Map contendo os dados extraídos (chaves: mesReferencia, anoReferencia,
     *         consumokWh, valorFatura, e demais campos disponíveis)
     * @throws IOException se ocorrer erro na leitura do PDF
     */
    public Map<String, Object> parse(InputStream pdfStream) throws IOException {

        // Converte InputStream → byte[] (Loader.loadPDF não aceita InputStream <- Erro que estava ocorrendo)
        byte[] pdfBytes = pdfStream.readAllBytes();
        
        // --- Variáveis de extração ---
        String  nomeCliente       = "";
        int     codigoCliente     = 0;
        int     codigoInstalacao  = 0;
        String  ucNumero          = "";
        String  ucSubgrupo        = "";
        String  ucFornecimento    = "";
        String  referencia        = "";
        int     mesReferencia     = 0;
        int     anoReferencia     = 0;
        float   consumokWh        = 0;
        float   valorFatura       = 0;
        String  boletoVencimento  = "";
        String  boletoNumero      = "";
        long    notafiscal        = 0;
        String  chaveNF3e         = "";

        // Código que estava apresentando erro
        //try (PDDocument document = Loader.loadPDF(pdfStream)) {
        
        // Código corrigido. Recebe byte[]
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();

            List<String> linhas = stripper.getText(document)
                    .lines()
                    .map(String::trim)
                    .filter(linha -> !linha.isEmpty())
                    .toList();

            for (int i = 0; i < linhas.size(); i++) {
                String linha = linhas.get(i);

                // Nome do cliente
                if (linha.toUpperCase().contains("NOME DO CLIENTE")) {
                    if (i + 1 < linhas.size()) {
                        nomeCliente = linhas.get(i + 1);
                    }
                    continue;
                }

                // Código do cliente
                if (linha.equalsIgnoreCase("CÓDIGO DO CLIENTE")) {
                    if (i + 1 < linhas.size()) {
                        String valor = linhas.get(i + 1).trim();
                        if (valor.matches("\\d+")) {
                            codigoCliente = Integer.parseInt(valor);
                        }
                    }
                    continue;
                }

                // Código da instalação
                if (linha.contains("CÓDIGO DA INSTALAÇÃO")) {
                    if (i + 1 < linhas.size()) {
                        codigoInstalacao = Integer.parseInt(linhas.get(i + 1));
                    }
                    continue;
                }

                // Unidade Consumidora Número
                if (linha.toUpperCase().contains("UNIDADE CONSUMIDORA")) {
                    int posicao = linha.lastIndexOf(" e ");
                    if (posicao != -1) {
                        ucNumero = linha.substring(posicao + 3).trim();
                    }
                    continue;
                }

                // Unidade Consumidora Subgrupo e Tipo de Fornecimento
                if (linha.contains("CLASSIFICAÇÃO")) {
                    int inicioClass = linha.indexOf("CLASSIFICAÇÃO:") + "CLASSIFICAÇÃO:".length();
                    int inicioTipo = linha.indexOf("TIPO DE FORNECIMENTO:");
                    if (inicioTipo > inicioClass) {
                        ucSubgrupo = linha.substring(inicioClass, inicioTipo).trim();
                    }
                    if (inicioTipo != -1) {
                        int inicioForn = inicioTipo + "TIPO DE FORNECIMENTO:".length();
                        ucFornecimento = linha.substring(inicioForn).trim();
                    }
                    continue;
                }

                // Referência da fatura
                if (linha.equalsIgnoreCase("REF:MÊS/ANO")) {
                    if (i + 1 < linhas.size()) {
                        referencia = linhas.get(i + 1).trim();
                        String[] partes = referencia.split("/");
                        mesReferencia = Integer.parseInt(partes[0]);
                        anoReferencia = Integer.parseInt(partes[1]);
                    }
                    continue;
                }

                // Consumo da fatura em kWh
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

                // Vencimento da fatura
                if (linha.equalsIgnoreCase("VENCIMENTO")) {
                    if (i + 1 < linhas.size()) {
                        boletoVencimento = linhas.get(i + 1);
                    }
                    continue;
                }

                // Número do boleto
                if (linha.toUpperCase().contains("PAGÁVEL EM QUALQUER REDE BANCÁRIA")) {
                    if (i + 1 < linhas.size()) {
                        boletoNumero = linhas.get(i + 1).replaceAll("[^0-9]", "").trim();
                    }
                    continue;
                }

                // Nota Fiscal
                if (linha.startsWith("NOTA FISCAL N°")) {
                    String numero = linha.replace("NOTA FISCAL N°", "").trim();
                    numero = numero.substring(0, numero.indexOf("-")).trim();
                    notafiscal = Long.parseLong(numero);
                    continue;
                }

                // Chave NF3e
                if (linha.toUpperCase().contains("CHAVE DE ACESSO")) {
                    if (i + 1 < linhas.size()) {
                        chaveNF3e = linhas.get(i + 1).replace(" ", "").trim();
                    }
                    continue;
                }
            }
        }

        // --- Monta o mapa de retorno com os 4 campos prioritários + extras ---
        Map<String, Object> dados = new HashMap<>();
        dados.put("mesReferencia",    mesReferencia);
        dados.put("anoReferencia",    anoReferencia);
        dados.put("consumokWh",       consumokWh);
        dados.put("valorFatura",      valorFatura);

        // Demais campos disponíveis para uso futuro
        dados.put("nomeCliente",      nomeCliente);
        dados.put("codigoCliente",    codigoCliente);
        dados.put("codigoInstalacao", codigoInstalacao);
        dados.put("ucNumero",         ucNumero);
        dados.put("ucSubgrupo",       ucSubgrupo);
        dados.put("ucFornecimento",   ucFornecimento);
        dados.put("referencia",       referencia);
        dados.put("boletoVencimento", boletoVencimento);
        dados.put("boletoNumero",     boletoNumero);
        dados.put("notafiscal",       notafiscal);
        dados.put("chaveNF3e",        chaveNF3e);

        return dados;
    }
}
