package formulas.multaDeMora

VALIDACAO(lancamento.dataVencimento != null, "Data de vencimento não pode ser nulo.")

def dataMudancaRegimento = Date.parse("d/M/yyyy", "03/12/2008");
def mesReferenciaFinal = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
def diasAtraso =  MAXIMO(mesReferenciaFinal - lancamento.dataVencimento, 0)
def limite = SE(lancamento.dataVencimento > dataMudancaRegimento, 0.20, 0.10)
def taxa = MINIMO(diasAtraso * 0.0033, limite);
lancamento.multaMora = TRUNC(lancamento.valorOriginal * taxa, 2)