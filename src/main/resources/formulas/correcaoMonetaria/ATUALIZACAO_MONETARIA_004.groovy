package formulas.correcaoMonetaria

/***********************************************************************************************************
 * Cálculo de Atualização monetária para Receita de Autorização para Uso de Radiofrequências - 1,9 GHz e 2,5 GHz
 *
 * Item 5.7, alíneas "a" e "c", do Edital de Licitação nº 2/2015-SOR/SPR/CD-ANATEL
 * Variação do IGP-DI, desde a data da entrega dos Documentos de Identificação e de Regularidade Fiscal,
 * das Propostas de Preço e da Documentação de Habilitação até a data do efetivo pagamento. Caso o
 * pagamento ocorra após 12 (doze) meses da data de entrega desses documentos, além da atualização pelo
 * IGP-DI, os valores são acrescidos de juros simples de 0,25% (vinte e cinco centésimos percentuais)ao mês,
 * incidentes sobre o valor corrigido, desde a data de publicação, no Diário Oficial da União, do extrato
 * do Termo de Autorização
 **********************************************************************************************************/

VALIDACAO(parametros["DATA_ENTREGA_DOCUMENTACAO"] != null, "Data de entrega dos documentos de habilitação não informado")
VALIDACAO(parametros["DATA_PUBLICACAO_AUTORIZACAO"] != null, "Data de publicação do extrato de autorização não informado")

def dataInicioCobranca = parametros["DATA_ENTREGA_DOCUMENTACAO"]
def dataFinalCobranca = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
def taxaAtualizacao = SE(dataInicioCobranca <= dataFinalCobranca, INDICE_ECONOMICO("IGP-DI", dataInicioCobranca, dataFinalCobranca), 0.00)
lancamento.atualizacaoMonetaria = lancamento.valorOriginal * taxaAtualizacao

if(dataFinalCobranca > DATA_ADICIONAR(parametros["DATA_ENTREGA_DOCUMENTACAO"], 12, "M")) {
    def quantidadeMeses = MAXIMO(DATADIF(parametros["DATA_PUBLICACAO_AUTORIZACAO"], dataFinalCobranca, "M"), 0.00)
    lancamento.atualizacaoMonetaria += lancamento.valorAtualizado * (quantidadeMeses * 0.0025)
}

// Tratamento de casas decimais para a atualização monetária
lancamento.atualizacaoMonetaria = TRUNC(lancamento.atualizacaoMonetaria, 2)