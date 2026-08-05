<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Ajustes do Usuário</title>
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
            border-bottom: 1px solid #eee;
            padding-bottom: 15px;
            margin-bottom: 20px;
        }
        h3 {
            margin-top: 25px;
            margin-bottom: 15px;
            color: #2c3e50;
        }
        label {
            font-weight: bold;
            font-size: 14px;
            color: #333;
        }
        input[type="text"],
        input[type="email"],
        input[type="password"],
        input[type="date"] {
            width: 100%;
            padding: 8px 10px;
            margin: 4px 0 12px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 14px;
        }
        .linha-dupla {
            display: flex;
            gap: 20px;
        }
        .linha-dupla > div {
            flex: 1;
        }
        .mensagem {
            padding: 10px 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .mensagem-sucesso {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .mensagem-erro {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .botoes {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 25px;
            border-top: 1px solid #eee;
            padding-top: 20px;
        }
        .btn-voltar {
            padding: 10px 20px;
            cursor: pointer;
            background: #95a5a6;
            color: #fff;
            border: none;
            border-radius: 5px;
            font-size: 14px;
        }
        .btn-salvar {
            padding: 10px 24px;
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

    <h2>Ajustes do usuário</h2>

    <!-- ==================== MENSAGENS DE FEEDBACK ==================== -->
    <c:if test="${not empty sucesso}">
        <div class="mensagem mensagem-sucesso">${sucesso}</div>
    </c:if>
    <c:if test="${not empty erro}">
        <div class="mensagem mensagem-erro">${erro}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/ajustes-usuario">

        <!-- ==================== DADOS DE ACESSO ==================== -->
        <h3>Dados de acesso</h3>

        <label>E-mail:</label>
        <input type="email" name="email" value="${usuario.email}" required>

        <label>Nova senha:</label>
        <input type="password" name="senhaNova" placeholder="Deixe em branco para manter a atual" minlength="6">

        <label>Confirmar nova senha:</label>
        <input type="password" name="senhaConfirmacao" placeholder="Repita a nova senha">

        <!-- ==================== DADOS DE PESSOA FÍSICA ==================== -->
        <c:if test="${usuario.tipo == 'PF' || usuario.tipo == 'A'}">
            <h3>Dados de Pessoa Física</h3>

            <div class="linha-dupla">
                <div>
                    <label>Nome:</label>
                    <input type="text" name="nome" value="${usuario.nome}" required>
                </div>
                <div>
                    <label>Sobrenome:</label>
                    <input type="text" name="sobrenome" value="${usuario.sobrenome}" required>
                </div>
            </div>

            <div class="linha-dupla">
                <div>
                    <label>CPF:</label>
                    <input type="text" name="cpf" value="${usuario.cpf}" required>
                </div>
                <div>
                    <label>RG:</label>
                    <input type="text" name="rg" value="${usuario.rg}">
                </div>
            </div>

            <label>Data de nascimento:</label>
            <input type="date" name="dataNascimento" value="${usuario.dataNascimento}">
        </c:if>

        <!-- ==================== DADOS DE PESSOA JURÍDICA ==================== -->
        <c:if test="${usuario.tipo == 'PJ' || usuario.tipo == 'F'}">
            <h3>Dados de Pessoa Jurídica</h3>

            <div class="linha-dupla">
                <div>
                    <label>Razão Social:</label>
                    <input type="text" name="razaoSocial" value="${usuario.razaoSocial}" required>
                </div>
                <div>
                    <label>Nome Fantasia:</label>
                    <input type="text" name="nomeFantasia" value="${usuario.nomeFantasia}">
                </div>
            </div>

            <div class="linha-dupla">
                <div>
                    <label>CNPJ:</label>
                    <input type="text" name="cnpj" value="${usuario.cnpj}" required>
                </div>
                <div>
                    <label>Inscrição Estadual:</label>
                    <input type="text" name="inscricaoEstadual" value="${usuario.inscricaoEstadual}">
                </div>
            </div>
        </c:if>

        <!-- ==================== ENDEREÇO E CONTATO ==================== -->
        <h3>Endereço e contato</h3>

        <div class="linha-dupla">
            <div>
                <label>Logradouro:</label>
                <input type="text" name="logradouro" value="${usuario.logradouro}">
            </div>
            <div>
                <label>Número:</label>
                <input type="text" name="logNumero" value="${usuario.logNumero}">
            </div>
        </div>

        <label>Complemento:</label>
        <input type="text" name="complemento" value="${usuario.complemento}">

        <div class="linha-dupla">
            <div>
                <label>Cidade:</label>
                <input type="text" name="cidade" value="${usuario.cidade}">
            </div>
            <div>
                <label>Estado:</label>
                <input type="text" name="estado" maxlength="2" value="${usuario.estado}">
            </div>
        </div>

        <label>CEP:</label>
        <input type="text" name="cep" value="${usuario.cep}">

        <div class="linha-dupla">
            <div>
                <label>Telefone 1:</label>
                <input type="text" name="telefone1" value="${usuario.telefone1}">
            </div>
            <div>
                <label>Telefone 2:</label>
                <input type="text" name="telefone2" value="${usuario.telefone2}">
            </div>
        </div>

        <!-- ==================== BOTÕES ==================== -->
        <div class="botoes">
            <button type="button" class="btn-voltar" onclick="window.location.href='${pageContext.request.contextPath}/painel'">← Voltar</button>
            <button type="submit" class="btn-salvar">Salvar alterações</button>
        </div>

    </form>

</div>

</body>
</html>
