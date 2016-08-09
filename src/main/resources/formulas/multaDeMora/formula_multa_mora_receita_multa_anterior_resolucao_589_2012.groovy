package formulas.multaDeMora

/***********************************************************************************************************
 * Cálculo de Multa de Mora para Multas sem Suspenção de Exigibilidade

 Multas sem a interposição de recurso administrativo ou tendo sido negado o pedido de
 efeito suspensivo
 Regras de Atualização
 Para o cálculo da atualização das multas que se enquadram nesta situação, há que combinar o
 exposto na Resolução 344, com o anexo do Informe nº 19-ADPF/SA

 **********************************************************************************************************/

VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo')

def dataResolucaoAnatel589_2012 = Date.parse("d/MM/yyyy", "17/05/2012")
if(!lancamento.houveSuspencaoExigibilidade && lancamento.dataCompetencia < dataResolucaoAnatel589_2012){

    if(lancamento.houvePagamentoParcial
            && parametros["DATA_AVISO_RECEBIMENTO"] != null
            && lancamento.dataPagamento <= parametros["DATA_AVISO_RECEBIMENTO"] + 30
            && lancamento.valorPago >= lancamento.valorOriginal) {
        lancamento.multaMora = 0.00
    } else {
        // Calcula quantidade de dias em atraso
        def mesReferenciaFinal = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
        def diasEmAtraso = MAXIMO(mesReferenciaFinal - lancamento.dataVencimento, 0)

        // Determina o percentual de multa a ser aplicado
        def taxa = MINIMO(diasEmAtraso * 0.0033, 0.10)

        // Calculo da multa de mora considerando apenas 2 casas decimais
        lancamento.multaMora = TRUNC((lancamento.valorOriginal * taxa), 2)
    }
}
