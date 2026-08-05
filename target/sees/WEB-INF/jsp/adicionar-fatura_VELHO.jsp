<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Fatura</title>
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
        h2 {
            margin-top: 0;
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
        .botoes {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 20px;
        }
        .btn-voltar {
            padding: 8px 16px;
            cursor: pointer;
            background: #95a5a6;
            color: #fff;
            border: none;
            border-radius: 5px;
            font-size: 14px;
        }
        .btn-salvar {
            padding: 8px 20px;
            cursor: pointer;
            background: #2980b9;
            color: #fff;
            border: none;
            border-radius: 5px;
            font-size: 14px;
        }
    </style>
</head>
<body>

<div class="container">

    <h2>Cadastro de Fatura de Energia</h2>

    <form method="post" action="${pageContext.request.contextPath}/adicionar-fatura/manual">

        <table>
            <tr>
                <th>Mês (1-12)</th>
                <th>Ano</th>
                <th>Consumo (kWh)</th>
                <th>Valor Pago (R$)</th>
            </tr>
            <tr>
                <td>
                    <input type="number" name="mes" min="1" max="12" required>
                </td>
                <td>
                    <input type="number" name="ano" min="2000" max="2100" required>
                </td>
                <td>
                    <input type="number" name="consumo" step="0.01" min="0" required>
                </td>
                <td>
                    <input type="number" name="valor" step="0.01" min="0" required>
                </td>
            </tr>
        </table>

        <div class="botoes">
            <button type="button" class="btn-voltar" onclick="history.back()">← Voltar</button>
            <button type="submit" class="btn-salvar">Salvar fatura</button>
        </div>

    </form>

</div>

</body>
</html>
