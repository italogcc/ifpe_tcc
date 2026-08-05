<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>SEES</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/web/css/index.css">
</head>
<body>

    <h1>Sistema de Economia por Energia Solar</h1>
    <img src="${pageContext.request.contextPath}/web/img/PlacaSolar.jpg" alt="Placas solares" style="max-width:400px; border-radius:8px;">
    <div class="botoes" style="margin-top:20px;">
        <a class="botao" href="${pageContext.request.contextPath}/login">Login</a>
        <a class="botao" href="${pageContext.request.contextPath}/registrar">Registrar</a>
        <a class="botao" href="${pageContext.request.contextPath}/ajuda">Ajuda</a>
    </div>

</body>
</html>
