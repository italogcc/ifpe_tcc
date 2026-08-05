<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Painel do Usuário</title>
</head>
<body>

<!-- ==================== HEADER COM LOGOUT ==================== -->
<div style="display: flex; justify-content: space-between; align-items: center;">
    <h2>Bom dia, ${usuario.nome}!</h2>
    <form action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit">Sair</button>
    </form>
</div>

<!-- ==================== FATURAS INCOMPLETAS ==================== -->
<c:if test="${!faturasCompletas}">
    <p>Você ainda não possui 3 faturas de energia cadastradas.</p>
    <p>Você tem <strong>${qtdFaturas}</strong> fatura(s) cadastradas.</p>
    <p>São necessárias 3 faturas para verificação das médias de consumo.</p>

    <a href="${pageContext.request.contextPath}/adicionar-fatura">
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
    <table border="1">
        <tr>
            <th>Mês</th>
            <th>Ano</th>
            <th>Consumo (kWh)</th>
            <th>Valor (R$)</th>
            <th>Ação</th>
        </tr>
        <c:forEach var="fatura" items="${faturas}">
            <tr>
                <td>${fatura.mesReferencia}</td>
                <td>${fatura.anoReferencia}</td>
                <td><fmt:formatNumber value="${fatura.consumokWh}" pattern="0.00" /></td>
                <td><fmt:formatNumber value="${fatura.valorFatura}" pattern="0.00" /></td>
                <td>
                    <a href="${pageContext.request.contextPath}/editar-fatura?mes=${fatura.mesReferencia}&ano=${fatura.anoReferencia}">
                        Editar
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <br>
    <a href="${pageContext.request.contextPath}/editar-fatura">
        <button>Editar faturas</button>
    </a>
</c:if>

</body>
</html>
