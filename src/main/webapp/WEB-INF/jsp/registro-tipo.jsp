<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Registrar usuário</title>
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
            max-width: 450px;
        }
        h2 {
            margin-top: 0;
            text-align: center;
            border-bottom: 1px solid #eee;
            padding-bottom: 15px;
            margin-bottom: 20px;
            color: #2c3e50;
        }
        .lista-tipos {
            list-style: none;
            padding: 0;
            margin: 0;
        }
        .lista-tipos li {
            margin-bottom: 10px;
        }
        .lista-tipos a {
            display: block;
            padding: 12px 16px;
            background: #ecf0f1;
            color: #2c3e50;
            text-decoration: none;
            border-radius: 5px;
            font-size: 15px;
            border: 1px solid #ddd;
            transition: background 0.2s, border-color 0.2s;
        }
        .lista-tipos a:hover {
            background: #2980b9;
            color: #fff;
            border-color: #2980b9;
        }
        .botoes-rodape {
            display: flex;
            justify-content: flex-start;
            align-items: center;
            margin-top: 25px;
            border-top: 1px solid #eee;
            padding-top: 15px;
        }
        .btn-voltar {
            padding: 8px 16px;
            background: #95a5a6;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
        }
        .btn-voltar:hover {
            background: #7f8c8d;
        }
    </style>
</head>
<body>

<div class="container">

    <h2>Qual tipo de usuário deseja registrar?</h2>

    <ul class="lista-tipos">
        <li>
            <a href="${pageContext.request.contextPath}/registrar/formulario?tipo=PF">Pessoa Física</a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/registrar/formulario?tipo=PJ">Pessoa Jurídica</a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/registrar/formulario?tipo=F">Fornecedor</a>
        </li>
        <li>
            <a href="${pageContext.request.contextPath}/registrar/formulario?tipo=A">Administrador do Sistema</a>
        </li>
    </ul>

    <div class="botoes-rodape">
        <a href="${pageContext.request.contextPath}/" class="btn-voltar">← Voltar</a>
    </div>

</div>

</body>
</html>
