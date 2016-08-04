VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo.')
def diasDeAtraso = MAXIMO(DATA_REFERENCIA - lancamento.dataVencimento, 0)
def taxa = MINIMO(diasDeAtraso * 0.0033, 0.20)
lancamento.multaMora = TRUNC((lancamento.valorOriginal * taxa), 2)