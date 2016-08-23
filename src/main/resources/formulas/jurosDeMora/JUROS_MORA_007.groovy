package formulas.jurosDeMora

/***********************************************************************************************************
 * Cálculo de Juros de Mora para Receitas Ressarcimento Ligações Telefônicas
 *
 * Juros equivalentes à taxa referencial do Sistema Especial de Liquidação e Custódia – SELIC, acumulada mensalmente,
 * a partir do 31º posterior a data de recebimento do comunicado de cobrança e de 1% no mês do pagamento
 **********************************************************************************************************/

VALIDACAO(parametros["DATA_RECEBIMENTO_COMUNICADO_COBRANCA"] != null, 'Data do recebimento do comunicação de cobrança não informado')

// Considera o juros a partir do 31º posterior a data de recebimento do comunicado de cobrança
def dataComunicado = parametros["DATA_RECEBIMENTO_COMUNICADO_COBRANCA"]
def dataInicioCobranca = dataComunicado.copyWith(date: 01, month: dataComunicado[Calendar.MONTH] + 1)

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