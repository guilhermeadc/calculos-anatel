package formulas.multaDeMora

/***********************************************************************************************************
 * Cálculo de Multa de Mora para Multas sem Suspenção de Exigibilidade anteriores à 17/05/2016

 Multas sem a interposição de recurso administrativo ou tendo sido negado o pedido de efeito suspensivo

 Regras de Atualização
 Para o cálculo da atualização das multas que se enquadram nesta situação, há que combinar o
 exposto na Resolução 344, com o anexo do Informe nº 19-ADPF/SA

 **********************************************************************************************************/

VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo')

dataResolucaoAnatel589_2012 = Date.parse("d/MM/yyyy", "17/05/2012")
if(!lancamento.houveSuspencaoExigibilidade && lancamento.dataCompetencia < dataResolucaoAnatel589_2012) {

    if(lancamento.houvePagamentoParcial
            && lancamento.dataPagamento != null
            && parametros["DATA_AVISO_RECEBIMENTO"] != null
            && parametros["DATA_AVISO_RECEBIMENTO"] + 30 >= lancamento.dataPagamento
            && lancamento.valorPago >= lancamento.valorOriginal) {
        lancamento.multaMora = 0.00
    } else {
        // Calcula quantidade de dias em atraso
        mesReferenciaInicial = (parametros["DATA_AVISO_RECEBIMENTO"] != null) ? parametros["DATA_AVISO_RECEBIMENTO"] + 31 : lancamento.dataVencimento

        //TODO: Tratar o caso de entendimento sobre a data de pagamento para pagamentos parciais
        mesReferenciaFinal = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
        diasEmAtraso = MAXIMO(mesReferenciaFinal - mesReferenciaInicial, 0)

        // Determina o percentual de multa a ser aplicado
        dataMudancaRegimento = Date.parse("d/M/yyyy", "03/12/2008");
        limite = SE(lancamento.dataVencimento >= dataMudancaRegimento, 0.20, 0.10)
        taxa = MINIMO(diasEmAtraso * 0.0033, limite)

        // Calculo da multa de mora considerando apenas 2 casas decimais
        lancamento.multaMora = TRUNC(lancamento.valorOriginal * taxa, 2)
    }
}
