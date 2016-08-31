package formulas.jurosDeMora

/***********************************************************************************************************
 * Cálculo de Juros de Mora para Multas sem Suspenção de Exigibilidade aplicadas antes da Resolução Anatel no 589/2012

 Multas sem a interposição de recurso administrativo ou tendo sido negado o pedido de
 efeito suspensivo

 Regras de Atualização
 Para o cálculo da atualização das multas que se enquadram nesta situação, há que combinar o
 exposto na Resolução 344, com o anexo do Informe nº 19-ADPF/SA

 **********************************************************************************************************/

VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo')

dataResolucaoAnatel589_2012 = DATA("17/05/2012")
if(!lancamento.houveSuspencaoExigibilidade && lancamento.dataCompetencia < dataResolucaoAnatel589_2012) {

    if(lancamento.houvePagamentoParcial
            && lancamento.dataPagamento != null
            && parametros["DATA_AVISO_RECEBIMENTO"] != null
            && parametros["DATA_AVISO_RECEBIMENTO"] + 30 >= lancamento.dataPagamento
            && lancamento.valorPago >= lancamento.valorOriginal) {
        lancamento.jurosMora = 0.00
    } else {
        // Considera a juros a partir do primeiro dia do mês subseqüente ao vencimento do prazo...
        mesReferenciaInicial = (parametros["DATA_AVISO_RECEBIMENTO"] != null) ? parametros["DATA_AVISO_RECEBIMENTO"] + 30 : lancamento.dataVencimento
        mesInicio = mesReferenciaInicial.copyWith(date: 01, month: mesReferenciaInicial[Calendar.MONTH] + 1)

        //TODO: Tratar o caso de entendimento sobre a data de pagamento para pagamentos parciais
        // ... até o mês anterior ao do pagamento
        mesReferenciaFinal = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
        mesFinal = mesReferenciaFinal.copyWith(date: 01, month: mesReferenciaFinal[Calendar.MONTH]) - 1

        // ... e de um por cento no mês de pagamento
        indiceAcumulado = SE(mesInicio <= mesReferenciaFinal, INDICE_ECONOMICO("SELIC", mesInicio, mesFinal) + 0.01, 0.00)

        // Calculo do juros de mora considerando apenas 2 casas decimais
        lancamento.jurosMora = TRUNC(lancamento.valorAtualizado * indiceAcumulado, 2)

        //Log: Utilizado apenas para validação do cálculo
        LOG_CALCULO["mes_inicio"] = mesInicio.format("MM/yyyy")
        LOG_CALCULO["mes_final"] = mesFinal.format("MM/yyyy")
        LOG_CALCULO["indice_aplicado"] = indiceAcumulado
    }
}
