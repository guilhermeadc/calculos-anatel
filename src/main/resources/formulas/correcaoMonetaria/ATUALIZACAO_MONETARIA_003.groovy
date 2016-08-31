package formulas.correcaoMonetaria

/***********************************************************************************************************
 * Cálculo de Atualização monetária considerando Juros equivalentes à taxa SELIC
 *
 * Art. 37-B, §3º, da Lei 10.522/2002
 * Juros equivalentes à taxa referencial do Sistema Especial de Liquidação e Custódia – SELIC,
 * acumulada mensalmente, a partir do mês subsequente ao vencimento do prazo e de 1% no mês do pagamento
 **********************************************************************************************************/

VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo.')

// Considera a juros a partir do primeiro dia do mês subseqüente ao vencimento do prazo...
dataInicioCobranca = lancamento.dataVencimento.copyWith(date: 01, month: lancamento.dataVencimento[Calendar.MONTH] + 1)

// ... até o mês anterior ao do pagamento
mesReferenciaFinal = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
dataFinalCobranca = mesReferenciaFinal.copyWith(date: 01, month: mesReferenciaFinal[Calendar.MONTH]) - 1

// ... e de um por cento no mês de pagamento
indiceAcumulado = SE(dataInicioCobranca <= mesReferenciaFinal, INDICE_ECONOMICO("SELIC", dataInicioCobranca, dataFinalCobranca) + 0.01, 0.00)

// Calculo do juros de mora considerando apenas 2 casas decimais
lancamento.atualizacaoMonetaria = TRUNC(lancamento.valorOriginal * indiceAcumulado, 2)

//Log: Utilizado apenas para validação do cálculo
LOG_CALCULO["mes_inicio"] = dataInicioCobranca.format("MM/yyyy")
LOG_CALCULO["mes_final"] = dataFinalCobranca.format("MM/yyyy")
LOG_CALCULO["indice_aplicado"] = indiceAcumulado