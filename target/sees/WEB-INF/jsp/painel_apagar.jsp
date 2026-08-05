<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Painel do Usuário</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f4f6f8;
            margin: 0;
            padding: 40px 20px;
            display: flex;
            justify-content: center;
        }
        .container {
            background: #fff;
            padding: 30px 40px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
            width: 100%;
            max-width: 800px;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #eee;
            padding-bottom: 15px;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        th, td {
            padding: 10px;
            text-align: center;
            border: 1px solid #ddd;
        }
        th { background: #f0f0f0; }
        .acoes { display: flex; gap: 10px; justify-content: center; align-items: center; }
        .acoes img { width: 20px; height: 20px; border: 0; }
        .status-ok { color: green; font-weight: bold; }
        .status-nao { color: #b00; font-weight: bold; }
        .botoes { margin-top: 20px; display: flex; gap: 10px; justify-content: center; }
        button, .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
            display: inline-block;
        }
        .btn-danger { background: #c0392b; color: #fff; }
        .btn-primary { background: #2980b9; color: #fff; }
        .btn-secondary { background: #7f8c8d; color: #fff; }
    </style>
</head>
<body>

<div class="container">

    <!-- ==================== HEADER COM LOGOUT ==================== -->
    <div class="header">
        <h2>Bom dia, ${usuario.nome}!</h2>
        <form action="${pageContext.request.contextPath}/logout" method="post">
            <button type="submit" class="btn btn-secondary">Sair</button>
        </form>
    </div>

    <!-- ==================== FATURAS INCOMPLETAS ==================== -->
    <c:if test="${!faturasCompletas}">
        <p>Você ainda não possui 3 faturas de energia cadastradas.</p>
        <p>Você tem <strong>${qtdFaturas}</strong> fatura(s) cadastradas.</p>
        <p>São necessárias 3 faturas para verificação das médias de consumo.</p>

        <a href="${pageContext.request.contextPath}/adicionar-fatura" class="btn btn-primary">
            Cadastrar nova fatura
        </a>
    </c:if>

    <!-- ==================== FATURAS COMPLETAS ==================== -->
    <c:if test="${faturasCompletas}">
        <p>Suas 3 faturas já foram cadastradas. Confira o resumo:</p>

        <ul>
            <li><strong>Consumo médio:</strong>
                <fmt:formatNumber value="${mediaConsumo}" pattern="0.00" /> kWh
            </li>
            <li><strong>Valor médio:</strong>
                R$ <fmt:formatNumber value="${mediaValor}" pattern="0.00" />
            </li>
        </ul>

        <h3>Faturas cadastradas</h3>
        <table>
            <tr>
                <th>Mês</th>
                <th>Ano</th>
                <th>Consumo (kWh)</th>
                <th>Valor (R$)</th>
                <th>Editar</th>
                <th>Upload PDF</th>
            </tr>
            <c:forEach var="fatura" items="${faturas}">
                <tr>
                    <td>${fatura.mesReferencia}</td>
                    <td>${fatura.anoReferencia}</td>
                    <td><fmt:formatNumber value="${fatura.consumokWh}" pattern="0.00" /></td>
                    <td><fmt:formatNumber value="${fatura.valorFatura}" pattern="0.00" /></td>

                    <!-- EDITAR com imagem -->
                    <td>
                        <a href="${pageContext.request.contextPath}/editar-fatura?mes=${fatura.mesReferencia}&ano=${fatura.anoReferencia}" title="Editar fatura">
                            <img src="${pageContext.request.contextPath}/web/img/editar.png" alt="Editar">
                        </a>
                    </td>

                    <!-- UPLOAD com status OK / Não -->
                    <td>
                        <c:choose>
                            <c:when test="${fatura.pdfEnviado}">
                                <span class="status-ok">OK</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-nao">Não</span>
                            </c:otherwise>
                        </c:choose>
                        &nbsp;
                        <button type="button" class="btn btn-primary"
                                onclick="abrirUpload(${fatura.mesReferencia}, ${fatura.anoReferencia})">
                            Upload
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <!-- ==================== BOTÃO LIMPAR FATURAS ==================== -->
        <div class="botoes">
            <button type="button" class="btn btn-danger" onclick="limparFaturas()">
                Limpar faturas
            </button>
        </div>
    </c:if>

</div>

<!-- ==================== FORM OCULTO DE UPLOAD ==================== -->
<form id="formUpload" action="${pageContext.request.contextPath}/upload-fatura" method="post" enctype="multipart/form-data" style="display:none;">
    <input type="hidden" name="mes" id="uploadMes">
    <input type="hidden" name="ano" id="uploadAno">
    <input type="file" name="arquivoPdf" id="arquivoPdf" accept="application/pdf">
</form>

<!-- ==================== FORM OCULTO DE LIMPAR ==================== -->
<form id="formLimpar" action="${pageContext.request.contextPath}/limpar-faturas" method="post" style="display:none;"></form>

<script>
    // ===== UPLOAD DE PDF =====
    function abrirUpload(mes, ano) {
        document.getElementById('uploadMes').value = mes;
        document.getElementById('uploadAno').value = ano;
        const fileInput = document.getElementById('arquivoPdf');
        fileInput.value = ''; // limpa seleção anterior
        fileInput.click();
    }

    document.getElementById('arquivoPdf').addEventListener('change', function (e) {
        const arquivo = e.target.files[0];
        if (!arquivo) return;

        // Validação: apenas PDF
        if (arquivo.type !== 'application/pdf') {
            alert('Selecione um arquivo PDF válido.');
            e.target.value = '';
            return;
        }
        // Validação: limite de 10MB
        const tamanhoMax = 10 * 1024 * 1024; // 10 MB
        if (arquivo.size > tamanhoMax) {
            alert('O arquivo excede o limite de 10 MB.');
            e.target.value = '';
            return;
        }
        // Envia o formulário (o backend chamará ParserFaturaNeoenergiaPE.java)
        document.getElementById('formUpload').submit();
    });

    // ===== LIMPAR FATURAS COM CONFIRMAÇÃO =====
    function limparFaturas() {
        if (confirm('Tem certeza que deseja apagar TODAS as faturas cadastradas? Esta ação não pode ser desfeita.')) {
            document.getElementById('formLimpar').submit();
        }
    }
</script>

</body>
</html>
