package formulas.correcaoMonetaria

/***********************************************************************************************************
 * Cálculo de atualização monetária para Multas com Suspenção de Exigibilidade anteriores à Resolução Anatel no 589/2012
 *
 * A concessão de efeito suspensivo em sede de recurso administrativo ou pedido de reconsideração
 * suspende a exigência de pagamento da multa desde a data de registro na Anatel até a data da
 * decisão que não dê provimento ao recurso ou ao pedido de reconsideração.

 * Incorrendo a suspensão do crédito motivada pela apresentação de recurso com efeito suspensivo,
 * o crédito deverá sofrer atualização monetária pelo índice IGP-DI desde a data de aplicação da
 * sanção até dezembro de 2008. A partir de janeiro de 2009, a correção pelo IGP-DI é substituída
 * pela aplicação da SELIC até a data de publicação do extrato da decisão final no Diário Oficial da
 * União.
 **********************************************************************************************************/

dataResolucaoAnatel589_2012 = Date.parse("d/MM/yyyy", "17/05/2012")
if(lancamento.dataCompetencia < dataResolucaoAnatel589_2012
        && lancamento.houveSuspencaoExigibilidade) {

    // Considera a atualização deste a data de aplicação da sanção (data de competência)
    dataAplicacaoSancao = lancamento.dataCompetencia

    //... até a data de publicação do extrato da decisão final no Diário Oficial da União ou a data atual
    dataPublicacaoDOU = MINIMO(parametros["DATA_PUBLICAOCAO_DOU"], DATA_REFERENCIA)

    // Data de publicação da Medida Provisória 449/2008
    dataMedidaProvisoria449_2008 = Date.parse("d/MM/yyyy", "03/12/2008")
    dataLimiteCalculoIGPDI = Date.parse("d/MM/yyyy", "31/12/2008")
    dataLimiteCalculoSelic = Date.parse("d/MM/yyyy", "01/01/2009")

    // crédito deverá sofrer atualização monetária pelo índice IGP-DI desde a data de aplicação da sanção até dezembro de 2008
    indiceAcumuladoIGPDI = SE(dataAplicacaoSancao < dataMedidaProvisoria449_2008, INDICE_ECONOMICO("IGP-DI", dataAplicacaoSancao, dataLimiteCalculoIGPDI), 0.00)
    lancamento.atualizacaoMonetaria = lancamento.valorOriginal * MAXIMO(indiceAcumuladoIGPDI, 0.00)

    // a partir de janeiro de 2009, a correção pelo IGP-DI é substituída pela aplicação da SELIC até a
    // data de publicação do extrato da decisão final no Diário Oficial da União.
    dataInicioCalculoSelic = MAXIMO(dataLimiteCalculoSelic, dataAplicacaoSancao)
    indiceAcumuladoSelic = SE(dataPublicacaoDOU >= dataLimiteCalculoSelic, INDICE_ECONOMICO("SELIC", dataInicioCalculoSelic, dataPublicacaoDOU), 0.00)
    lancamento.atualizacaoMonetaria += lancamento.valorOriginal * MAXIMO(indiceAcumuladoSelic, 0.00)
}
