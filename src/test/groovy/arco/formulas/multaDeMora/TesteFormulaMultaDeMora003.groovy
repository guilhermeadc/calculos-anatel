package arco.formulas.multaDeMora

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaMultaDeMora003 extends Specification {

    def script = "MULTA_MORA_003.groovy"
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
        lancamento.dataPagamento = data_pagamento ? Date.parse("d/M/yyyy", data_pagamento) : null
        lancamento.dataVencimento = data_vencimento ? Date.parse("d/M/yyyy", data_vencimento) : null
        lancamento.valorOriginal = valor_original
        expect:
        def resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.multaMora == multa_mora
        resultado.valorTotal == valor_total

        where:
        valor_original | data_vencimento | data_pagamento | data_atual   || multa_mora | valor_total
        1000.00        | "01/06/2016"    | null           | "01/05/2016" || 0          | 1000.00
        1000.00        | "01/06/2016"    | null           | "01/06/2016" || 0          | 1000.00
        1000.00        | "01/06/2016"    | null           | "02/06/2016" || 3.30       | 1003.30
        1000.00        | "01/06/2016"    | null           | "01/07/2016" || 99.00      | 1099.00
        1000.00        | "01/06/2015"    | null           | "01/01/2016" || 200.00     | 1200.00
        1000.00        | "01/06/2007"    | null           | "01/05/2007" || 0          | 1000.00
        1000.00        | "01/06/2007"    | null           | "01/06/2007" || 0          | 1000.00
        1000.00        | "01/06/2007"    | null           | "02/06/2007" || 3.30       | 1003.30
        1000.00        | "01/06/2007"    | null           | "01/07/2007" || 99.00      | 1099.00
        1000.00        | "01/06/2006"    | null           | "01/01/2007" || 100.00     | 1100.00
        1000.00        | "01/06/2016"    | "01/05/2016"   | "01/01/2050" || 0          | 1000.00
        1000.00        | "01/06/2016"    | "01/06/2016"   | "01/01/2050" || 0          | 1000.00
        1000.00        | "01/06/2016"    | "02/06/2016"   | "01/01/2050" || 3.30       | 1003.30
        1000.00        | "01/06/2016"    | "01/07/2016"   | "01/01/2050" || 99.00      | 1099.00
        1000.00        | "01/06/2015"    | "01/01/2016"   | "01/01/2050" || 200.00     | 1200.00
        1000.00        | "01/06/2007"    | "01/05/2007"   | "01/01/2050" || 0          | 1000.00
        1000.00        | "01/06/2007"    | "01/06/2007"   | "01/01/2050" || 0          | 1000.00
        1000.00        | "01/06/2007"    | "02/06/2007"   | "01/01/2050" || 3.30       | 1003.30
        1000.00        | "01/06/2007"    | "01/07/2007"   | "01/01/2050" || 99.00      | 1099.00
        1000.00        | "01/06/2006"    | "01/01/2007"   | "01/01/2050" || 100.00     | 1100.00

    }

    def "Teste de calculo de multa de mora sem data de vencimento"() {
        when: "Quando a fórmula é executada com lancamento sem data de vencimento"
        calculadora.executarFormula(script, lancamento, parametros)

        then:
        thrown(AssertionError)
    }
}
