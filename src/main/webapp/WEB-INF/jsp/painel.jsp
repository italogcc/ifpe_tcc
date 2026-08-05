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
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: flex-start;
            padding-top: 40px;
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
        .header-buttons {
            display: flex;
            gap: 10px;
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
        .link-cadastrar {
            display: inline-block;
            margin-top: 10px;
            padding: 8px 16px;
            background: #2980b9;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
        }
        .btn-sair { 
            padding: 6px 12px; 
            cursor: pointer; 
            background: #e74c3c;
            color: #fff;
            border: none;
            border-radius: 5px;
        }
        .btn-ajustes {
            padding: 6px 12px;
            cursor: pointer;
            background: #95a5a6;
            color: #fff;
            border: none;
            border-radius: 5px;
        }

        /* === NOVO: Estilos dos botões de ação === */
        .acao-links {
            display: flex;
            gap: 8px;
            justify-content: center;
        }
        .link-editar {
            color: #2980b9;
            text-decoration: none;
            font-weight: bold;
        }
        .link-editar:hover {
            text-decoration: underline;
        }
        .btn-excluir {
            background: none;
            border: none;
            color: #e74c3c;
            cursor: pointer;
            font-weight: bold;
            font-size: 14px;
            padding: 0;
        }
        .btn-excluir:hover {
            text-decoration: underline;
        }
        /* ==================================== */
    </style>
</head>
<body>

<div class="container">

    <!-- ==================== HEADER COM LOGOUT E AJUSTES ==================== -->
    <div class="header">
        <h2>Bom dia, ${usuario.nome}!</h2>
        <div class="header-buttons">
            <form action="${pageContext.request.contextPath}/ajustes-usuario" method="get" style="display: inline;">
                <button type="submit" class="btn-ajustes">Ajustes do usuário</button>
            </form>
            <form action="${pageContext.request.contextPath}/logout" method="post" style="display: inline;">
                <button type="submit" class="btn-sair">Sair</button>
            </form>
        </div>
    </div>

    <!-- ==================== FATURAS INCOMPLETAS ==================== -->
    <c:if test="${!faturasCompletas}">
        <p>Você ainda não possui 3 faturas de energia cadastradas.</p>
        <p>Você tem <strong>${qtdFaturas}</strong> fatura(s) cadastradas.</p>
        <p>São necessárias 3 faturas para verificação das médias de consumo.</p>

        <a href="${pageContext.request.contextPath}/adicionar-fatura" class="link-cadastrar">
            Cadastrar nova fatura
        </a>

        <!-- Tabela com faturas já cadastradas (se houver) -->
        <c:if test="${not empty faturas}">
            <h3>Faturas cadastradas</h3>
            <table>
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
                            <div class="acao-links">
                                <!-- Editar -->
                                <a href="${pageContext.request.contextPath}/editar-fatura?mes=${fatura.mesReferencia}&ano=${fatura.anoReferencia}"
                                   class="link-editar">Editar</a>

                                <!-- Excluir -->
                                <form action="${pageContext.request.contextPath}/excluir-fatura"
                                      method="post" style="display: inline;"
                                      onsubmit="return confirm('Tem certeza que deseja excluir a fatura ${fatura.mesReferencia}/${fatura.anoReferencia}?')">
                                    <input type="hidden" name="mes" value="${fatura.mesReferencia}">
                                    <input type="hidden" name="ano" value="${fatura.anoReferencia}">
                                    <button type="submit" class="btn-excluir">Excluir</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
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
                <th>Ação</th>
            </tr>
            <c:forEach var="fatura" items="${faturas}">
                <tr>
                    <td>${fatura.mesReferencia}</td>
                    <td>${fatura.anoReferencia}</td>
                    <td><fmt:formatNumber value="${fatura.consumokWh}" pattern="0.00" /></td>
                    <td><fmt:formatNumber value="${fatura.valorFatura}" pattern="0.00" /></td>
                    <td>
                        <div class="acao-links">
                            <!-- Editar -->
                            <a href="${pageContext.request.contextPath}/editar-fatura?mes=${fatura.mesReferencia}&ano=${fatura.anoReferencia}"
                               class="link-editar">Editar</a>

                            <!-- Excluir -->
                            <form action="${pageContext.request.contextPath}/excluir-fatura"
                                  method="post" style="display: inline;"
                                  onsubmit="return confirm('Tem certeza que deseja excluir a fatura ${fatura.mesReferencia}/${fatura.anoReferencia}?')">
                                <input type="hidden" name="mes" value="${fatura.mesReferencia}">
                                <input type="hidden" name="ano" value="${fatura.anoReferencia}">
                                <button type="submit" class="btn-excluir">Excluir</button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <br>
        <a href="${pageContext.request.contextPath}/adicionar-fatura">
            <button>Cadastrar nova fatura</button>
        </a>
    </c:if>

</div>

</body>
</html>
