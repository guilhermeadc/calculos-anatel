package formulas.jurosDeMora

VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo.')

// Considera a juros a partir do primeiro dia do mês subseqüente ao vencimento do prazo...
def dataInicioCobranca = lancamento.dataVencimento.copyWith(date: 01, month: lancamento.dataVencimento[Calendar.MONTH] + 1)

// ... até o mês anterior ao do pagamento
def mesReferenciaFinal = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
def dataFinalCobranca = mesReferenciaFinal.copyWith(date: 01, month: mesReferenciaFinal[Calendar.MONTH]) - 1

// ... e de um por cento no mês de pagamento
def indiceAcumulado = SE(dataInicioCobranca <= mesReferenciaFinal, INDICE_ECONOMICO("SELIC", dataInicioCobranca, dataFinalCobranca) + 0.01, 0.00)

// Calculo do juros de mora considerando apenas 2 casas decimais
lancamento.jurosMora = TRUNC(lancamento.valorAtualizado * indiceAcumulado, 2)

//Log: Utilizado apenas para validação do cálculo
LOG_CALCULO["mes_inicio"] = dataInicioCobranca.format("MM/yyyy")
LOG_CALCULO["mes_final"] = dataFinalCobranca.format("MM/yyyy")
LOG_CALCULO["indice_aplicado"] = indiceAcumulado