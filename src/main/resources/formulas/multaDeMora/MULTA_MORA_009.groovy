package formulas.multaDeMora

/***********************************************************************************************************
 * Cálculo de Multa de Mora para Receitas Ressarcimento Ligações Telefônicas
 *
 * 0,33% (zero vírgula trinta e três por cento) por dia de atraso, até o limite de 20% (vinte por cento),
 * calculada a partir do 31º posterior a data de recebimento do comunicado de cobrança, até o dia em que ocorrer o seu pagamento
 **********************************************************************************************************/

VALIDACAO(parametros["DATA_RECEBIMENTO_COMUNICADO_COBRANCA"] != null, 'Data do recebimento do comunicação de cobrança não informado')

// Calcula quantidade de dias em atraso
dataFinalCobranca = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
diasEmAtraso = MAXIMO(dataFinalCobranca - parametros["DATA_RECEBIMENTO_COMUNICADO_COBRANCA"], 0)

// Determina o percentual de multa a ser aplicado
taxa = MINIMO(diasEmAtraso * 0.0033, 0.20)

// Calculo da multa de mora considerando apenas 2 casas decimais
lancamento.multaMora = TRUNC(lancamento.valorOriginal * taxa, 2)