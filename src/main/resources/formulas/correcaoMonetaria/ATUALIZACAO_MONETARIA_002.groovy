package formulas.correcaoMonetaria

/***********************************************************************************************************
 * Cálculo de atualização monetária para Multas com Suspenção de Exigibilidade posteriores à Resolução Anatel no 589/2012
 *
 * Resolução Anatel no 589/2012
 * § 1o, Art. 34. Tendo sido negado provimento ou seguimento ao recurso
 * administrativo ou ao pedido de reconsideração o valor da multa ser paga deve
 * sofrer correção segundo a Taxa Referencial do Sistema Especial de Liquidação e
 * Custódia (Selic) ou de outro índice que vier a substituí-lo, conforme a legislação
 * em vigor, desde a data da intimação da cominação da multa até a data de
 * intimação da decisão definitiva.
 *
 * Havendo o registro do recurso com efeito suspensivo, o crédito deverá sofrer atualização
 * monetária pela SELIC desde a Data de Intimação até a Data de Constituição da multa.
 *
 **********************************************************************************************************/

VALIDACAO(parametros["DATA_INTIMACAO_MULTA"] != null, "Data de intimação da multa não informado")

dataResolucaoAnatel589_2012 = Date.parse("d/MM/yyyy", "17/05/2012")
if(lancamento.dataCompetencia >= dataResolucaoAnatel589_2012
        && lancamento.houveSuspencaoExigibilidade) {

    //TODO: Verificar se a data de competência realmente é a data de intimação da multa
    // Considera a atualização deste a data de intimação da multa (data de competência)
    dataIntimacaoMulta = parametros["DATA_INTIMACAO_MULTA"]

    //... até a data de constituição da multa ou a data atual
    datatConstituicaoMulta = MINIMO(parametros["DATA_CONSTITUICAO_MULTA"], DATA_REFERENCIA)

    indiceAcumuladoSelic = INDICE_ECONOMICO("SELIC", dataIntimacaoMulta, datatConstituicaoMulta)
    lancamento.atualizacaoMonetaria = lancamento.valorOriginal * MAXIMO(indiceAcumuladoSelic, 0.00)
}
