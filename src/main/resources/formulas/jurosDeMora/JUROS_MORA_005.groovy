package formulas.jurosDeMora

/***********************************************************************************************************
 * Cálculo de Juros de Mora para Multas com Suspenção de Exigibilidade anteriores à Resolução Anatel no 589/2012
 **********************************************************************************************************/

//TODO: Avaliar seguinte declaração: A suspensão da exigibilidade, objeto da medida liminar, não afasta a incidência de juros de mora;
def dataResolucaoAnatel589_2012 = DATA("17/05/2012")
if(lancamento.dataCompetencia < dataResolucaoAnatel589_2012
        && lancamento.houveSuspencaoExigibilidade
        && parametros["DATA_PUBLICAOCAO_DOU"] != null) {

    //TODO: Verificar se a data de competência realmente é a data de intimação da multa
    // Considera a juros a partir do primeiro dia do mês subseqüente ao vencimento do prazo...
    def mesReferenciaInicial = parametros["DATA_PUBLICAOCAO_DOU"] + 30
    def mesInicio = mesReferenciaInicial.copyWith(date: 01, month: mesReferenciaInicial[Calendar.MONTH] + 1)

    //TODO: Tratar o caso de entendimento sobre a data de pagamento para pagamentos parciais
    // ... até o mês anterior ao do pagamento
    def mesReferenciaFinal = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
    def mesFinal = mesReferenciaFinal.copyWith(date: 01, month: mesReferenciaFinal[Calendar.MONTH]) - 1

    // ... e de um por cento no mês de pagamento
    def indiceAcumulado = SE(mesInicio <= mesReferenciaFinal, INDICE_ECONOMICO("SELIC", mesInicio, mesFinal) + 0.01, 0.00)

    // Calculo do juros de mora considerando apenas 2 casas decimais
    lancamento.jurosMora = TRUNC(lancamento.valorAtualizado * indiceAcumulado,2)

    //Log: Utilizado apenas para validação do cálculo
    LOG_CALCULO["mes_inicio"] = mesInicio.format("MM/yyyy")
    LOG_CALCULO["mes_final"] = mesFinal.format("MM/yyyy")
    LOG_CALCULO["indice_aplicado"] = indiceAcumulado
}
