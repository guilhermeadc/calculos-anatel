package formulas.jurosDeMora

/***********************************************************************************************************
 * Cálculo de Juros de Mora para Multas sem Suspenção de Exigibilidade aplicadas após a Resolução Anatel no 589/2012

 Resolução Anatel no 589/2012
 Art. 36. Quando não houver pagamento da multa nos prazos definidos neste Capítulo, o seu valor deve ser
 acrescido dos seguintes encargos:

 II - juros de mora, contados do primeiro dia do mês subsequente ao do vencimento, equivalentes à taxa
 referencial do Sistema Especial de Liquidação e Custódia (Selic), para títulos federais, acumulada
 mensalmente, até o último dia do mês anterior ao pagamento, e de 1% (um por cento) no mês do
 pagamento.

 **********************************************************************************************************/

def dataResolucaoAnatel589_2012 = DATA("17/05/2012")
if(!lancamento.houveSuspencaoExigibilidade && lancamento.dataCompetencia >= dataResolucaoAnatel589_2012){

    VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo')

    // Considera a juros a partir do primeiro dia do mês subseqüente ao vencimento do prazo...
    def mesInicio = lancamento.dataVencimento.copyWith(date: 01, month: lancamento.dataVencimento[Calendar.MONTH] + 1)

    // ... até o mês anterior ao do pagamento
    def mesReferenciaFinal = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
    mesFinal = mesReferenciaFinal.copyWith(date: 01, month: mesReferenciaFinal[Calendar.MONTH]) - 1

    // ... e de um por cento no mês de pagamento
    def indiceAcumulado = SE(mesInicio <= mesReferenciaFinal, INDICE_ECONOMICO("SELIC", mesInicio, mesFinal) + 0.01, 0.00)

    // Calculo do juros de mora considerando apenas 2 casas decimais
    lancamento.jurosMora = TRUNC(lancamento.valorAtualizado * indiceAcumulado,2)

    //Log: Utilizado apenas para validação do cálculo
    LOG_CALCULO["mes_inicio"] = mesInicio.format("MM/yyyy")
    LOG_CALCULO["mes_final"] = mesFinal.format("MM/yyyy")
    LOG_CALCULO["indice_aplicado"] = indiceAcumulado
}
