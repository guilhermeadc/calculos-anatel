package formulas.correcaoMonetaria

/***********************************************************************************************************
 * Cálculo de Atualização monetária para Receita de Outorga de Autorização-Uso de Blocos de Radiofrequências – EDITAL 4G (700 MHz)
 *
 * Variação do IGP-DI, desde a data da entrega dos Documentos de Identificação e de Regularidade Fiscal, das Propostas
 * de Preço e da Documentação de Habilitação até a data do efetivo pagamento. Esses valores serão acrescidos,
 * além da atualização pelo IGP-DI, juros simples de 1,0% (um por cento) ao mês, incidentes sobre o valor
 * corrigido, desde a data de publicação, no Diário Oficial da União – DOU, do extrato do Termo de Autorização.
 **********************************************************************************************************/

VALIDACAO(parametros["DATA_ENTREGA_DOCUMENTACAO"] != null, "Data de entrega dos documentos de habilitação não informado")
VALIDACAO(parametros["DATA_PUBLICACAO_AUTORIZACAO"] != null, "Data de publicação do extrato de autorização não informado")

def dataInicioCobranca = parametros["DATA_ENTREGA_DOCUMENTACAO"]
def dataFinalCobranca = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
def taxaAtualizacao = SE(dataInicioCobranca <= dataFinalCobranca, INDICE_ECONOMICO("IGP-DI", dataInicioCobranca, dataFinalCobranca), 0.00)
lancamento.atualizacaoMonetaria = lancamento.valorOriginal * taxaAtualizacao

def quantidadeMeses = MAXIMO(DATADIF(parametros["DATA_PUBLICACAO_AUTORIZACAO"], dataFinalCobranca, "M"), 0.00)
lancamento.atualizacaoMonetaria += lancamento.valorAtualizado * (quantidadeMeses * 0.01)

// Tratamento de casas decimais para a atualização monetária
lancamento.atualizacaoMonetaria = TRUNC(lancamento.atualizacaoMonetaria, 2)