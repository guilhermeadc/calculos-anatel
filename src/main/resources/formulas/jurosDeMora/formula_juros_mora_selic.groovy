package formulas.jurosDeMora

VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo.')

// Considera a juros a partir do primeiro dia do mês subseqüente ao vencimento do prazo...
def mesInicio = lancamento.dataVencimento.copyWith(date: 01, month: lancamento.dataVencimento[Calendar.MONTH] + 1)

// ... até o mês anterior ao do pagamento
def mesReferenciaFinal = (lancamento.dataPagamento ?: DATA_REFERENCIA)
mesFinal = mesReferenciaFinal.copyWith(date: 01, month: mesReferenciaFinal[Calendar.MONTH]) - 1

// ... e de um por cento no mês de pagamento
def indiceAcumulado = SE(mesInicio <= mesReferenciaFinal, INDICE_ECONOMICO("SELIC", mesInicio, mesFinal) + 0.01, 0.00)

// Calculo do juros de mora considerando apenas 2 casas decimais
lancamento.jurosMora = TRUNC((lancamento.valorOriginal * indiceAcumulado),2)

//Log: Utilizado apenas para validação do cálculo
LOG_CALCULO["mes_inicio"] = mesInicio.format("MM/yyyy")
LOG_CALCULO["mes_final"] = mesFinal.format("MM/yyyy")
LOG_CALCULO["indice_aplicado"] = indiceAcumulado