package arco.formulas.jurosDeMora

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaJurosDeMora009 extends Specification {

    def script = "JUROS_MORA_009.groovy"
    def calculadora = null
    def lancamento = null
    def parametros = null

    def setup() {
        calculadora = new Calculadora()
        lancamento = new Lancamento(valorOriginal: 1000.00, dataLancamento: new Date())
        parametros = [:]
    }

    def "Teste de calculo de juros de mora"() {
        setup:
        lancamento.valorOriginal = valor_original
        lancamento.dataVencimento = data_vencimento ? Date.parse("d/MM/yyyy", data_vencimento) : null
        lancamento.dataPagamento = data_pagamento ? Date.parse("d/MM/yyyy", data_pagamento) : null
        calculadora.DATA_REFERENCIA = data_atual ? Date.parse("d/MM/yyyy", data_atual) : new Date()

        expect:
        def resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.jurosMora == juros_mora
        resultado.valorTotal == valor_total
        //Validação dos calculos intermediários da formula
        calculadora.logUltimoCalculo["indice_aplicado"] == indice_aplicado


        where:
        valor_original | data_vencimento | data_pagamento | data_atual   || indice_aplicado | juros_mora | valor_total
        1000.00        | "01/06/2016"    | null           | "01/05/2016" || 0.00            | 0.00       | 1000.00
        1000.00        | "01/06/2016"    | null           | "01/06/2016" || 0.00            | 0.00       | 1000.00
        1000.00        | "01/06/2016"    | null           | "02/06/2016" || 0.00            | 0.00       | 1000.00
        1000.00        | "01/06/2016"    | null           | "01/07/2016" || 0.005           | 5.00       | 1005.00
        1000.00        | "01/06/2015"    | null           | "01/01/2016" || 0.035           | 35.00      | 1035.00
        1000.00        | "01/06/2016"    | "01/05/2016"   | "01/01/2050" || 0.00            | 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "01/06/2016"   | "01/01/2050" || 0.00            | 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "02/06/2016"   | "01/01/2050" || 0.00            | 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "01/07/2016"   | "01/01/2050" || 0.005           | 5.00       | 1005.00
        1000.00        | "01/06/2015"    | "01/01/2016"   | "01/01/2050" || 0.035           | 35.00      | 1035.00
    }

    def "Teste de calculo de juros de mora sem data de vencimento"() {
        when: "Quando a fórmula é executada com lancamento sem data de vencimento"
        calculadora.executarFormula(script, lancamento, parametros)

        then: "Então é esperado uma exceção"
        thrown(AssertionError)
    }

}
