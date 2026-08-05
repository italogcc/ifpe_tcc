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

        /* === NOVO: Botão Carregar fatura === */
        .upload-section {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 20px;
            padding: 14px 18px;
            background: #eaf2f8;
            border-radius: 8px;
            border: 1px dashed #2980b9;
        }
        .btn-carregar {
            padding: 10px 22px;
            cursor: pointer;
            background: #27ae60;
            color: #fff;
            border: none;
            border-radius: 5px;
            font-size: 14px;
            font-weight: bold;
            transition: background 0.2s;
        }
        .btn-carregar:hover {
            background: #219a52;
        }
        .btn-carregar:disabled {
            background: #95a5a6;
            cursor: not-allowed;
        }
        .upload-status {
            font-size: 13px;
            color: #555;
            display: none;
        }
        .upload-status.erro {
            color: #c0392b;
            display: inline;
        }
        .upload-status.sucesso {
            color: #27ae60;
            display: inline;
        }
        /* ================================ */

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

    <!-- ========== NOVO: Seção de upload ========== -->
    <div class="upload-section">
        <button type="button" id="btnCarregarFatura" class="btn-carregar">
            📄 Carregar fatura
        </button>
        <span id="uploadStatus" class="upload-status"></span>
    </div>

    <!-- Input file oculto, acionado programaticamente pelo botão -->
    <input type="file" id="pdfFileInput" accept=".pdf" style="display: none;" />
    <!-- =========================================== -->

    <form id="formFatura" method="post" action="${pageContext.request.contextPath}/adicionar-fatura/manual">

        <table>
            <tr>
                <th>Mês (1-12)</th>
                <th>Ano</th>
                <th>Consumo (kWh)</th>
                <th>Valor Pago (R$)</th>
            </tr>
            <tr>
                <td>
                    <input type="number" name="mes" id="mes" min="1" max="12" required>
                </td>
                <td>
                    <input type="number" name="ano" id="ano" min="2000" max="2100" required>
                </td>
                <td>
                    <input type="number" name="consumo" id="consumo" step="0.01" min="0" required>
                </td>
                <td>
                    <input type="number" name="valor" id="valor" step="0.01" min="0" required>
                </td>
            </tr>
        </table>

        <div class="botoes">
            <button type="button" class="btn-voltar" onclick="history.back()">← Voltar</button>
            <button type="submit" class="btn-salvar">Salvar fatura</button>
        </div>

    </form>

</div>

<!-- ========== NOVO: Script de upload ========== -->
<script>
    const btnCarregar    = document.getElementById('btnCarregarFatura');
    const fileInput      = document.getElementById('pdfFileInput');
    const statusEl       = document.getElementById('uploadStatus');

    // Abre o seletor de arquivo quando o botão é clicado
    btnCarregar.addEventListener('click', () => {
        fileInput.click();
    });

    // Quando um arquivo é selecionado, faz o upload automaticamente
    fileInput.addEventListener('change', async () => {
        const file = fileInput.files[0];
        if (!file) return;

        // Feedback visual de carregamento
        btnCarregar.disabled = true;
        btnCarregar.textContent = '⏳ Processando...';
        statusEl.className = 'upload-status';
        statusEl.textContent = 'Extraindo dados da fatura...';
        statusEl.style.display = 'inline';

        const formData = new FormData();
        formData.append('pdfFile', file);

        try {
            const response = await fetch('${pageContext.request.contextPath}/adicionar-fatura/upload', {
                method: 'POST',
                body: formData
            });

            if (!response.ok) {
                throw new Error('Erro ' + response.status + ' ao processar o PDF');
            }

            const dados = await response.json();

            // Preenche os campos do formulário com os dados extraídos
            document.getElementById('mes').value     = dados.mesReferencia;
            document.getElementById('ano').value     = dados.anoReferencia;
            document.getElementById('consumo').value = dados.consumokWh;
            document.getElementById('valor').value   = dados.valorFatura;

            // Feedback de sucesso
            statusEl.className = 'upload-status sucesso';
            statusEl.textContent = '✓ Fatura carregada com sucesso!';

        } catch (erro) {
            statusEl.className = 'upload-status erro';
            statusEl.textContent = '✗ ' + erro.message;
            console.error(erro);
        } finally {
            btnCarregar.disabled = false;
            btnCarregar.textContent = '📄 Carregar fatura';
            // Reseta o input para permitir selecionar o mesmo arquivo novamente
            fileInput.value = '';
        }
    });
</script>
<!-- =========================================== -->

</body>
</html>
