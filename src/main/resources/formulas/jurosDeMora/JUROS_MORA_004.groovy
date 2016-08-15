package formulas.multaDeMora

/***********************************************************************************************************
 * Cálculo de Juros de Mora para Multas com Suspenção de Exigibilidade posteriores à 17/05/2016
 **********************************************************************************************************/

def dataResolucaoAnatel589_2012 = Date.parse("d/MM/yyyy", "17/05/2012")
if(lancamento.dataCompetencia >= dataResolucaoAnatel589_2012
        && lancamento.houveSuspencaoExigibilidade
        && parametros["DATA_CONSTITUICAO_MULTA"] != null) {

    // Considera a juros a partir do primeiro dia do mês subseqüente ao vencimento do prazo...
    def mesReferenciaInicial = parametros["DATA_CONSTITUICAO_MULTA"] + 30
    def mesInicio = mesReferenciaInicial.copyWith(date: 01, month: mesReferenciaInicial[Calendar.MONTH] + 1)

    //TODO: Tratar o caso de entendimento sobre a data de pagamento para pagamentos parciais
    // ... até o mês anterior ao do pagamento
    def mesReferenciaFinal = (lancamento.dataPagamento ?: DATA_REFERENCIA)
    mesFinal = mesReferenciaFinal.copyWith(date: 01, month: mesReferenciaFinal[Calendar.MONTH]) - 1

    // ... e de um por cento no mês de pagamento
    def indiceAcumulado = SE(mesInicio <= mesReferenciaFinal, INDICE_ECONOMICO("SELIC", mesInicio, mesFinal) + 0.01, 0.00)

    // Calculo do juros de mora considerando apenas 2 casas decimais
    lancamento.jurosMora = TRUNC(lancamento.valorOriginal * indiceAcumulado,2)

    //Log: Utilizado apenas para validação do cálculo
    LOG_CALCULO["mes_inicio"] = mesInicio.format("MM/yyyy")
    LOG_CALCULO["mes_final"] = mesFinal.format("MM/yyyy")
    LOG_CALCULO["indice_aplicado"] = indiceAcumulado
}
