
VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo.')

// Calcula quantidade de dias em atraso
dataFinalCobranca = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
diasEmAtraso = MAXIMO(dataFinalCobranca - lancamento.dataVencimento, 0)

// Determina o percentual de multa a ser aplicado
taxa = MINIMO(diasEmAtraso * 0.0033, 0.10)

// Calculo da multa de mora considerando apenas 2 casas decimais
lancamento.multaMora = TRUNC(lancamento.valorOriginal * taxa, 2)
