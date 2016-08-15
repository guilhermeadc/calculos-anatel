package arco.formulas.multaDeMora

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaMultaDeMora005 extends Specification {

    def script = "MULTA_MORA_005.groovy"
    def calculadora = null
    def lancamento = null
    def parametros = null

    def setup() {
        calculadora = new Calculadora();
        lancamento = new Lancamento(valorOriginal: 1000.00, dataLancamento: new Date())
        parametros = [:]
    }

    def "Teste de calculo de multa de mora"() {
        setup:
        calculadora.DATA_REFERENCIA = data_atual ? Date.parse("d/M/yyyy", data_atual) : null
        lancamento.dataVencimento = data_vencimento ? Date.parse("d/M/yyyy", data_vencimento) : null
        lancamento.dataPagamento = data_pagamento ? Date.parse("d/M/yyyy", data_pagamento) : null
        lancamento.dataCompetencia = data_competencia ? Date.parse("d/M/yyyy", data_competencia) : null
        lancamento.houveSuspencaoExigibilidade = houve_suspencao_exibilidade
        lancamento.valorOriginal = valor_original
        lancamento.valorPago = valor_pago
        lancamento.atualizacaoMonetaria = acrescimo
        parametros["DATA_AVISO_RECEBIMENTO"] = data_ar ? Date.parse("d/M/yyyy", data_ar) : null

        expect:
        def resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.multaMora == multa_mora
        resultado.valorTotal == valor_total

        where:
        valor_original | acrescimo | valor_pago| data_vencimento | data_competencia | data_pagamento | houve_suspencao_exibilidade | data_ar      | data_atual   || multa_mora | valor_total
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "17/05/2012"     | null           | false                       | null         | "01/05/2016" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "18/05/2012"     | null           | false                       | null         | "01/06/2016" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "19/05/2012"     | null           | false                       | null         | "02/06/2016" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "20/05/2012"     | null           | false                       | null         | "01/07/2016" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2015"    | "21/05/2012"     | null           | false                       | null         | "01/01/2016" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "22/05/2012"     | "01/05/2016"   | false                       | null         | "01/01/2050" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "23/05/2012"     | "01/06/2016"   | false                       | null         | "01/01/2050" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "24/05/2012"     | "02/06/2016"   | false                       | null         | "01/01/2050" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "25/05/2012"     | "01/07/2016"   | false                       | null         | "01/01/2050" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2015"    | "26/05/2012"     | "01/01/2016"   | false                       | null         | "01/01/2050" || 0.00       | 1000.00
//        /* Execução do cálculo considerando data de competência anterior à mudança de regimento em 17/05/2012 */
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "16/05/2012"     | null           | false                       | null         | "01/05/2016" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "16/05/2012"     | null           | false                       | null         | "01/06/2016" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "16/05/2012"     | null           | false                       | null         | "02/06/2016" || 3.30       | 1003.30
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "16/05/2012"     | null           | false                       | null         | "01/07/2016" || 99.00      | 1099.00
//        1000.00        | 0.00      | 0.00      | "01/06/2015"    | "16/05/2012"     | null           | false                       | null         | "01/01/2016" || 200.00     | 1200.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "16/05/2012"     | "01/05/2016"   | false                       | null         | "01/01/2050" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "16/05/2012"     | "01/06/2016"   | false                       | null         | "01/01/2050" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "16/05/2012"     | "02/06/2016"   | false                       | null         | "01/01/2050" || 3.30       | 1003.30
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "16/05/2012"     | "01/07/2016"   | false                       | null         | "01/01/2050" || 99.00      | 1099.00
//        1000.00        | 0.00      | 0.00      | "01/06/2015"    | "16/05/2012"     | "01/01/2016"   | false                       | null         | "01/01/2050" || 200.00     | 1200.00
//        /* Execução do cálculo considerando suspenção de exibilidade */
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "17/05/2012"     | null           | true                        | null         | "01/05/2016" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "18/05/2012"     | null           | true                        | null         | "01/06/2016" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "19/05/2012"     | null           | true                        | null         | "02/06/2016" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "20/05/2012"     | null           | true                        | null         | "01/07/2016" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2015"    | "21/05/2012"     | null           | true                        | null         | "01/01/2016" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "22/05/2012"     | "01/05/2016"   | true                        | null         | "01/01/2050" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "23/05/2012"     | "01/06/2016"   | true                        | null         | "01/01/2050" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "24/05/2012"     | "02/06/2016"   | true                        | null         | "01/01/2050" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2016"    | "25/05/2012"     | "01/07/2016"   | true                        | null         | "01/01/2050" || 0.00       | 1000.00
//        1000.00        | 0.00      | 0.00      | "01/06/2015"    | "26/05/2012"     | "01/01/2016"   | true                        | null         | "01/01/2050" || 0.00       | 1000.00
//        /* Execução do cálculo considerando os limites de multa de mora em 10% ou 20%, baseado na data de 03/12/2008 */
//        1000.00        | 0.00      | 0.00      | "03/12/2008"    | "01/10/2008"     | null           | false                       | null         | "01/01/2016" || 200.00      | 1200.00
//        1000.00        | 0.00      | 0.00      | "03/12/2008"    | "01/10/2008"     | "01/01/2016"   | false                       | null         | "01/01/2050" || 200.00      | 1200.00
//        1000.00        | 0.00      | 0.00      | "02/12/2008"    | "01/10/2008"     | null           | false                       | null         | "01/01/2016" || 100.00      | 1100.00
//        1000.00        | 0.00      | 0.00      | "02/12/2008"    | "01/10/2008"     | "01/01/2016"   | false                       | null         | "01/01/2050" || 100.00      | 1100.00
//        /* Execução do cálculo considerando o registro de Aviso de Recebimento (AR), modificando a data limite de pagamento */
//        1000.00        | 0.00      | 500.00    | "01/06/2016"    | "16/05/2012"     | null           | false                       | null         | "15/07/2016" || 145.20      | 1145.20
//        1000.00        | 0.00      | 500.00    | "01/06/2016"    | "16/05/2012"     | null           | false                       | "01/07/2016" | "15/07/2016" || 0.00        | 1000.00
//        1000.00        | 0.00      | 500.00    | "01/05/2016"    | "16/05/2012"     | null           | false                       | "01/06/2016" | "15/07/2016" || 42.90       | 1042.90 //15 dias de multa, considerando a partir da data do AR
        1000.00        | 50.00     | 1000.00   | "01/05/2016"    | "16/05/2012"     | "30/06/2016"   | false                       | "01/06/2016" | "15/07/2016" || 0.00        | 1050.00
        //TODO: Tratar o caso de entendimento sobre a data de pagamento para pagamentos parciais
        //1000.00        | 500.00    | "01/05/2016"    | "16/05/2012"     | "30/06/2016"   | false                       | "01/06/2016" | "15/07/2016" || 42.90       | 1042.90
    }

    def "Teste de calculo de multa de mora sem data de vencimento"() {
        when: "Quando a fórmula é executada com lancamento sem data de vencimento"
        calculadora.executarFormula(script, lancamento, parametros)

        then:
        thrown(AssertionError)
    }
}
