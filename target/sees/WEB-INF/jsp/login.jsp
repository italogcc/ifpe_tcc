<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
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
            max-width: 400px;
        }
        h2 {
            margin-top: 0;
            text-align: center;
            border-bottom: 1px solid #eee;
            padding-bottom: 15px;
            margin-bottom: 20px;
        }
        label {
            font-weight: bold;
            font-size: 14px;
            color: #333;
        }
        input[type="email"],
        input[type="password"] {
            width: 100%;
            padding: 8px 10px;
            margin: 4px 0 12px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 14px;
        }
        .mensagem-erro {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
            padding: 10px 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
        }
        .btn-entrar {
            width: 100%;
            padding: 10px;
            background: #2980b9;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            font-weight: bold;
        }
        .btn-entrar:hover {
            background: #2471a3;
        }
        .botoes-rodape {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 20px;
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
        .link-registro {
            font-size: 13px;
            color: #2980b9;
            text-decoration: none;
        }
        .link-registro:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="container">

    <h2>Login</h2>

    <c:if test="${not empty erro}">
        <div class="mensagem-erro">
            <c:out value="${erro}" />
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login">

        <label>E-mail:</label>
        <input type="email" name="email" required>

        <label>Senha:</label>
        <input type="password" name="senha" required>

        <button type="submit" class="btn-entrar">Entrar</button>

    </form>

    <div class="botoes-rodape">
        <a href="${pageContext.request.contextPath}/" class="btn-voltar">← Voltar</a>
        <a href="${pageContext.request.contextPath}/registrar" class="link-registro">Não tem conta? Cadastre-se</a>
    </div>

</div>

</body>
</html>
