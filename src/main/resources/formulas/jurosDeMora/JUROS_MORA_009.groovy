package formulas.jurosDeMora

VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não informada')

// Considera a juros a partir do primeiro dia subseqüente ao vencimento do prazo...
//def dataInicioCobranca = DATA_ADICIONAR(lancamento.dataVencimento, 1, "D")
def dataInicioCobranca = lancamento.dataVencimento

// ... até a data do efetivo pagamento
def dataFinalCobranca = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)

// juros equivalentes à taxa de 0,5% ao mês, desde a data de seu vencimento até a data do efetivo pagamento
def indiceAcumulado = MAXIMO(DATADIF(dataInicioCobranca, dataFinalCobranca, "M"), 0.00) * 0.005

// Calculo do juros de mora considerando apenas 2 casas decimais
lancamento.jurosMora = TRUNC(lancamento.valorAtualizado * indiceAcumulado, 2)

//Log: Utilizado apenas para validação do cálculo
LOG_CALCULO["indice_aplicado"] = indiceAcumulado