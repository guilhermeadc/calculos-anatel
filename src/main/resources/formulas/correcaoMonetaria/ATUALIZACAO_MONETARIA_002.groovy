package formulas.correcaoMonetaria

/***********************************************************************************************************
 * Cálculo de atualização monetária para Multas com Suspenção de Exigibilidade posteriores à 17/05/2016
 *
 * Medida Provisória no 449, de 03 de dezembro de 2008
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

def dataResolucaoAnatel589_2012 = Date.parse("d/MM/yyyy", "17/05/2012")
if(lancamento.dataCompetencia >= dataResolucaoAnatel589_2012
        && lancamento.houveSuspencaoExigibilidade
        && parametros["DATA_CONSTITUICAO_MULTA"] != null) {

    //TODO: Verificar se a data de compoetência realmente é a data de intimação da multa
    // Considera a atualização deste a data de intimação da multa (data de competência)
    def dataIntimacaoMulta = lancamento.dataCompetencia

    //... até a data de constituição da multa
    def datatConstituicaoMulta = parametros["DATA_CONSTITUICAO_MULTA"]

    def indiceAcumuladoSelic = INDICE_ECONOMICO("SELIC", dataIntimacaoMulta, datatConstituicaoMulta)
    lancamento.atualizacaoMonetaria = lancamento.valorOriginal * MAXIMO(indiceAcumuladoSelic, 0.00)
}
