package com.sees.projeto.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ParserFaturaNeoenergiaPE_MAIN {

    public static void main(String[] args) throws IOException {

        File fatura = new File(
                "D:\\Projetos\\java\\IFPE\\sees3\\src\\main\\java\\sees\\pdf\\Neoenergia_3027955_2026-06.pdf");
        
        try (PDDocument document = Loader.loadPDF(fatura)) {
            PDFTextStripper stripper = new PDFTextStripper();
            
            // Obtem os dados da fatura de energia
            // Remove linhas vazias, espaços no início e no fim;
            List<String> linhas = stripper.getText(document)
                    .lines()
                    .map(String::trim)
                    .filter(linha -> !linha.isEmpty())
                    .toList();
            
            // Dados da fatura
            String nomeCliente = "";
            int codigoCliente = 0;
            int codigoInstalacao = 0;
            String ucNumero = "";
            String ucSubgrupo = "";
            String ucFornecimento = "";
            String referencia = "";
            int mesReferencia = 0;
            int anoReferencia = 0;
            float consumokWh = 0;
            float valorFatura = 0;
            String boletoVencimento = "";
            String boletoNumero = "";
            long notafiscal = 0;
            String chaveNF3e = "";
            
            // Exibe todas as linhas da fatura em PDF
            //System.out.println("========== TEXTO EXTRAÍDO ==========");
            //linhas.forEach(System.out::println);
                        
            // Extração dos dados
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
                        codigoInstalacao = Integer.parseInt(linhas.get(i + 1)) ;
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
                    // Subgrupo
                    int inicioClass = linha.indexOf("CLASSIFICAÇÃO:") + "CLASSIFICAÇÃO:".length();
                    int inicioTipo = linha.indexOf("TIPO DE FORNECIMENTO:");
                    if (inicioTipo > inicioClass) {
                        ucSubgrupo = linha.substring(inicioClass, inicioTipo).trim();
                    }
                    // Tipo de Fornecimento
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
                    // partes[0] = Texto Consumo-TUSD
                    // partes[1] = Texto kWh
                    // partes[2] = Consumo em kWh
                    consumokWh = Float.parseFloat(partes[2].replace(",", "."));
                    continue;
                }
                
                // Valor da fatura
                if (linha.equalsIgnoreCase("TOTAL A PAGAR R$")) {
                    if (i + 1 < linhas.size()) {
                        String valor = linhas.get(i + 1).replace("R$", "").replace(".", "").replace(",", ".").trim();
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
         
                // Número do boleto para pagamento
                if (linha.toUpperCase().contains("PAGÁVEL EM QUALQUER REDE BANCÁRIA")) {
                    if (i + 1 < linhas.size()) {
                        boletoNumero = linhas.get(i + 1).replaceAll("[^0-9]", "").trim();
                    }
                    continue;
                }
                
                // Número da Nota Fiscal
                if (linha.startsWith("NOTA FISCAL N°")) {
                    String numero = linha.replace("NOTA FISCAL N°", "").trim();
                    numero = numero.substring(0, numero.indexOf("-")).trim();
                    notafiscal = Long.parseLong(numero);
                    continue;
                }
                
                // Número da chave NF3e
               if (linha.toUpperCase().contains("CHAVE DE ACESSO")) {
                    if (i + 1 < linhas.size()) {
                        chaveNF3e = linhas.get(i + 1).replace(" ", "").trim();
                    }
                    continue;
               }
            }

            System.out.println("\n========== DADOS DA FATURA ==========");
            System.out.println("Nome do cliente: " + nomeCliente);
            System.out.println("Código do cliente: " + codigoCliente);
            System.out.println("Código de instalação: " + codigoInstalacao);
            System.out.println("Unidade Consumidora número: " + ucNumero);
            System.out.println("Unidade Consumidora subgrupo: " + ucSubgrupo);
            System.out.println("Unidade Consumidora fornecimento: " + ucFornecimento);
            System.out.println("Referência: " + referencia);
            System.out.println("Ano: " + anoReferencia);
            System.out.println("Mês: " + mesReferencia);
            System.out.println("Consumo em kWh: " + consumokWh);
            System.out.println("Valor da fatura: " + valorFatura);
            System.out.println("Vencimento do boleto: " + boletoVencimento);
            System.out.println("Número do boleto: " + boletoNumero);
            System.out.println("Número da Nota Fiscal: " + notafiscal);
            System.out.println("Número da chave NF3e: " + chaveNF3e);
        }
    }
}