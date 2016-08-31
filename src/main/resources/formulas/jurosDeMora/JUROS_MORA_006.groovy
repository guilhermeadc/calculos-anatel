package formulas.jurosDeMora

/***********************************************************************************************************
 * Cálculo de Juros de Mora para Fust - Multa de Ofício
 *
 * Correspondentes a 1% (um por cento) ao mês até dezembro de 2008, substituídos, a partir de janeiro de 2009,
 * pela taxa referencial do Sistema Especial de Liquidação e Custódia – SELIC, acumulada mensalmente, a partir
 * do mês subsequente ao vencimento do prazo e de 1% (um por cento) no mês do pagamento
 **********************************************************************************************************/

VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo.')

indiceAcumulado = 0.00
dataLimiteMudancaRegimento = DATA("31/12/2008")
dataInicioCobranca = lancamento.dataVencimento.copyWith(date: 01, month: lancamento.dataVencimento[Calendar.MONTH] + 1)
dataFinalCobranca = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)

//Correspondentes a 1% (um por cento) ao mês até dezembro de 2008
if(dataInicioCobranca <= dataLimiteMudancaRegimento) {
    dataInicioCobranca1Porcento = MINIMO(dataInicioCobranca, dataLimiteMudancaRegimento)
    dataFinalCobranca1Porcento = MINIMO(dataFinalCobranca, dataLimiteMudancaRegimento)
    diferencaMeses = DATADIF(dataInicioCobranca1Porcento, dataFinalCobranca1Porcento, "M")
    indiceAcumulado = (dataInicioCobranca1Porcento <= dataFinalCobranca) ? MAXIMO((diferencaMeses + 1) / 100, 0.00) : 0.00
}

// A partir de janeiro de 2009, atualiza pela taxa referencial do SELIC, acumulada mensalmente, a partir do mês subsequente ao vencimento do prazo
dataInicioCobrancaSelic = MAXIMO(dataInicioCobranca, dataLimiteMudancaRegimento + 1)
dataFinalCobrancaSelic = MAXIMO(dataFinalCobranca, dataLimiteMudancaRegimento + 1)
dataFinalCobrancaSelic = dataFinalCobrancaSelic.copyWith(date: 01, month: dataFinalCobrancaSelic[Calendar.MONTH]) - 1

// ... e de um por cento no mês de pagamento
indiceAcumulado += (dataInicioCobrancaSelic <= dataFinalCobranca) ? INDICE_ECONOMICO("SELIC", dataInicioCobrancaSelic, dataFinalCobrancaSelic) + 0.01 : 0.00

// Calculo do juros de mora considerando apenas 2 casas decimais
lancamento.jurosMora = TRUNC(lancamento.valorAtualizado * indiceAcumulado, 2)

//Log: Utilizado apenas para validação do cálculo
LOG_CALCULO["mes_inicio"] = dataInicioCobranca.format("MM/yyyy")
LOG_CALCULO["mes_final"] = MINIMO(dataFinalCobranca, dataFinalCobrancaSelic).format("MM/yyyy")
LOG_CALCULO["indice_aplicado"] = indiceAcumulado
