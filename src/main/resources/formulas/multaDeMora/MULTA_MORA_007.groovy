package formulas.multaDeMora

/***********************************************************************************************************
 * Cálculo de Multa de Mora para Multas com Suspenção de Exigibilidade anterior à 17/05/2016

 A partir da data de publicação do extrato da decisão final no Diário Oficial da União, conceder-se-á mais 30 dias
 para o recolhimento do valor, sem qualquer tipo de acréscimo.
 Transcorridos os 30 dias, não ocorrendo o pagamento, aplica-se, além de juros de mora, a correção descrita a seguir sobre o valor atualizado:
    * multa moratória limitada a 20% = aplicada, além dos juros de mora cabíveis, à sanção cujo prazo para recolhimento (DOU + 30) seja igual ou superior a 03/12/2008; ou
    * multa moratória limitada a 10% = aplicada, além dos juros de mora cabíveis, à sanção cujo prazo para recolhimento (DOU + 30) seja anterior ou igual a 02/12/2008.

 **********************************************************************************************************/

def dataResolucaoAnatel589_2012 = Date.parse("d/MM/yyyy", "17/05/2012")
if(lancamento.dataCompetencia < dataResolucaoAnatel589_2012
        && lancamento.houveSuspencaoExigibilidade
        && parametros["DATA_PUBLICAOCAO_DOU"] != null) {

    // Calcula quantidade de dias em atraso
    def diaReferenciaInicial = parametros["DATA_PUBLICAOCAO_DOU"] + 30
    def diaReferenciaFinal = MINIMO(lancamento.dataPagamento ?: DATA_REFERENCIA, DATA_REFERENCIA)
    def diasEmAtraso = MAXIMO(diaReferenciaFinal - diaReferenciaInicial, 0)

    // Determina o percentual de multa a ser aplicado
    def dataMudancaRegimento = Date.parse("d/M/yyyy", "03/12/2008");
    def limite = SE(diaReferenciaInicial >= dataMudancaRegimento, 0.20, 0.10)
    def taxa = MINIMO(diasEmAtraso * 0.0033, limite)

    // Calculo da multa de mora considerando apenas 2 casas decimais
    lancamento.multaMora = TRUNC(lancamento.valorOriginal * taxa, 2)
}
