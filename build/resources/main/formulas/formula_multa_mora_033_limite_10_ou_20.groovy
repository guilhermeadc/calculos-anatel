VALIDACAO(lancamento.dataVencimento != null, "Data de vencimento não pode ser nulo.")
def dataMudancaRegimento = Date.parse("d/M/yyyy", "02/12/2008");
def diasAtraso =  MAXIMO(DATA_REFERENCIA - lancamento.dataVencimento, 0)
def limite = SE(lancamento.dataVencimento > dataMudancaRegimento, 0.20, 0.10)
def taxa = MINIMO(diasAtraso * 0.0033, limite);
lancamento.multaMora = lancamento.valorOriginal * taxa