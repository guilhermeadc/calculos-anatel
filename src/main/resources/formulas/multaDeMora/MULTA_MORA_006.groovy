package formulas.multaDeMora

/***********************************************************************************************************
 * Cálculo de Multa de Mora para Multas com Suspenção de Exigibilidade posteriores à Resolução Anatel no 589/2012

O novo regulamento inova ao determinar que as multas aplicadas a partir de sua publicação,
quando da interposição recurso administrativo ou ao pedido de reconsideração, não mais serão
atualizadas com base nas datas de aplicação da sanção e de publicação no DOU, mas sim, tendo
com referência as datas de intimação e de ciência da decisão definitiva.

 **********************************************************************************************************/

dataResolucaoAnatel589_2012 = Date.parse("d/MM/yyyy", "17/05/2012")

if(lancamento.dataCompetencia >= dataResolucaoAnatel589_2012
        && lancamento.houveSuspencaoExigibilidade
        && parametros["DATA_CONSTITUICAO_MULTA"] != null) {

    // Calcula quantidade de dias em atraso
    diaReferenciaInicial = parametros["DATA_CONSTITUICAO_MULTA"] + 30
    diaReferenciaFinal = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
    diasEmAtraso = MAXIMO(diaReferenciaFinal - diaReferenciaInicial, 0)

    // Determina o percentual de multa a ser aplicado
    taxa = MINIMO(diasEmAtraso * 0.0033, 0.20)

    // Calculo da multa de mora considerando apenas 2 casas decimais
    lancamento.multaMora = TRUNC(lancamento.valorOriginal * taxa, 2)
}
