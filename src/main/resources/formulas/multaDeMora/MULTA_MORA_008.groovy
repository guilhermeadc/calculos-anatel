package formulas.jurosDeMora

/***********************************************************************************************************
 * Cálculo de Multa de Mora para Declaração Espontânea do FUST
 *
 * Art. 8º, §1º, do Decreto nº 3.624/2000 e
 * art. 37-A da Lei nº 10.522/2002 c/c Art. 61, §§ 1º e 2º, da Lei nº 9.430/1996
 * Percentual de 2% (dois por cento) do valor do crédito para os débitos vencidos até novembro de 2008 e
 * para os débitos vencidos a partir de dezembro de 2008, acréscimo de 0,33% (zero vírgula trinta e três por cento)
 * por dia de atraso, até o limite de 20% (vinte por cento), calculada a partir do primeiro dia subsequente ao
 * do vencimento do prazo fixado para pagamento, até o dia em que ocorrer o seu pagamento
 **********************************************************************************************************/

VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo.')

def dataLimiteMudancaRegimento = DATA("31/12/2008")

def dataFinalCobranca = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
def diasEmAtraso = MAXIMO(dataFinalCobranca - lancamento.dataVencimento, 0)

def taxa = 0
if(lancamento.dataVencimento > dataLimiteMudancaRegimento) {
    taxa = MINIMO(diasEmAtraso * 0.0033, 0.20)
} else {
    taxa = SE(diasEmAtraso > 0, 0.02, 0.00)
}

// Calculo da multa de mora considerando apenas 2 casas decimais
lancamento.multaMora = TRUNC(lancamento.valorOriginal * taxa, 2)