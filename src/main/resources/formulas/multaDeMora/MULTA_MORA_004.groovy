package formulas.multaDeMora

/***********************************************************************************************************
 * Cálculo de Multa de Mora para Multas sem Suspenção de Exigibilidade posteriores à 17/05/2016

 Art. 36. Quando não houver pagamento da multa nos prazos definidos neste Capítulo, o seu valor deve ser
 acrescido dos seguintes encargos:

 I - multa moratória de 0,33% (trinta e três centésimos por cento) por dia de atraso, até o limite de 20% (vinte
 por cento), calculada a partir do primeiro dia subsequente ao do vencimento do prazo para pagamento da
 sanção administrativa imputada definitivamente, até o dia em que ocorrer o seu pagamento, nos termos da
 legislação federal aplicável;

 **********************************************************************************************************/

VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo')

dataResolucaoAnatel589_2012 = Date.parse("d/MM/yyyy", "17/05/2012")
if(!lancamento.houveSuspencaoExigibilidade && lancamento.dataCompetencia >= dataResolucaoAnatel589_2012){

    // Calcula quantidade de dias em atraso
    mesReferenciaFinal = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
    diasEmAtraso = MAXIMO(mesReferenciaFinal - lancamento.dataVencimento, 0)

    // Determina o percentual de multa a ser aplicado
    taxa = MINIMO(diasEmAtraso * 0.0033, 0.20)

    // Calculo da multa de mora considerando apenas 2 casas decimais
    lancamento.multaMora = TRUNC((lancamento.valorOriginal * taxa), 2)
}
