package arco.formulas.jurosDeMora

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaJurosDeMora006 extends Specification {

    def script = "JUROS_MORA_006.groovy"
    def calculadora = null
    def lancamento = null
    def parametros = null

    def setup() {
        calculadora = new Calculadora()
        lancamento = new Lancamento(valorOriginal: 1000.00, dataLancamento: new Date())
        parametros = [:]
    }

    def "Teste de calculo de multa de mora"() {
        setup:
        lancamento.valorOriginal = valor_original
        lancamento.dataVencimento = data_vencimento ? Date.parse("d/MM/yyyy", data_vencimento) : null
        lancamento.dataPagamento = data_pagamento ? Date.parse("d/MM/yyyy", data_pagamento) : null
        calculadora.DATA_REFERENCIA = data_atual ? Date.parse("d/MM/yyyy", data_atual) : new Date()
        calculadora.metaClass.INDICE_ECONOMICO = {String a, Date b, Date c -> return indice_selic}

        expect:
        def resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.jurosMora == juros_mora
        resultado.valorTotal == valor_total
        //Validação dos calculos intermediários da formula
        calculadora.logUltimoCalculo["mes_inicio"] == mes_inicio_juros
        calculadora.logUltimoCalculo["mes_final"] == mes_final_juros
        calculadora.logUltimoCalculo["indice_aplicado"] == indice_aplicado

        where:
        valor_original | data_vencimento | data_pagamento | data_atual     | indice_selic || indice_aplicado | mes_inicio_juros | mes_final_juros | juros_mora | valor_total
        /* Testes de cálculo considerando período anterior à 01/01/2009 */
        1000.00        | "01/06/2008"    | null           | "01/05/2008"   | 0.00         || 0.00            | "07/2008"        | "05/2008"       | 0.00       | 1000.00
        1000.00        | "01/06/2008"    | null           | "01/06/2008"   | 0.00         || 0.00            | "07/2008"        | "06/2008"       | 0.00       | 1000.00
        1000.00        | "01/06/2008"    | null           | "02/06/2008"   | 0.00         || 0.00            | "07/2008"        | "06/2008"       | 0.00       | 1000.00
        1000.00        | "01/06/2008"    | null           | "01/07/2008"   | 0.00         || 0.01            | "07/2008"        | "07/2008"       | 10.00      | 1010.00
        1000.00        | "01/06/2007"    | null           | "01/06/2008"   | 0.00         || 0.12            | "07/2007"        | "06/2008"       | 120.00     | 1120.00
        1000.00        | "01/06/2008"    | "01/05/2008"   | "01/01/2050"   | 0.00         || 0.00            | "07/2008"        | "05/2008"       | 0.00       | 1000.00
        1000.00        | "01/06/2008"    | "01/06/2008"   | "01/01/2050"   | 0.00         || 0.00            | "07/2008"        | "06/2008"       | 0.00       | 1000.00
        1000.00        | "01/06/2008"    | "02/06/2008"   | "01/01/2050"   | 0.00         || 0.00            | "07/2008"        | "06/2008"       | 0.00       | 1000.00
        1000.00        | "01/06/2008"    | "01/07/2008"   | "01/01/2050"   | 0.00         || 0.01            | "07/2008"        | "07/2008"       | 10.00      | 1010.00
        1000.00        | "01/06/2007"    | "01/06/2008"   | "01/01/2050"   | 0.05         || 0.12            | "07/2007"        | "06/2008"       | 120.00     | 1120.00
        /* Testes de cálculo considerando período posterior à 01/01/2009 */
        1000.00        | "01/06/2009"    | null           | "01/05/2009"   | 0.00         || 0.00            | "07/2009"        | "04/2009"       | 0.00       | 1000.00
        1000.00        | "01/06/2009"    | null           | "01/06/2009"   | 0.00         || 0.00            | "07/2009"        | "05/2009"       | 0.00       | 1000.00
        1000.00        | "01/06/2009"    | null           | "02/06/2009"   | 0.00         || 0.00            | "07/2009"        | "05/2009"       | 0.00       | 1000.00
        1000.00        | "01/06/2009"    | null           | "01/07/2009"   | 0.00         || 0.01            | "07/2009"        | "06/2009"       | 10.00      | 1010.00
        1000.00        | "01/06/2009"    | null           | "01/06/2010"   | 0.11         || 0.12            | "07/2009"        | "05/2010"       | 120.00     | 1120.00
        1000.00        | "01/06/2009"    | "01/05/2009"   | "01/01/2050"   | 0.00         || 0.00            | "07/2009"        | "04/2009"       | 0.00       | 1000.00
        1000.00        | "01/06/2009"    | "01/06/2009"   | "01/01/2050"   | 0.00         || 0.00            | "07/2009"        | "05/2009"       | 0.00       | 1000.00
        1000.00        | "01/06/2009"    | "02/06/2009"   | "01/01/2050"   | 0.00         || 0.00            | "07/2009"        | "05/2009"       | 0.00       | 1000.00
        1000.00        | "01/06/2009"    | "01/07/2009"   | "01/01/2050"   | 0.00         || 0.01            | "07/2009"        | "06/2009"       | 10.00      | 1010.00
        1000.00        | "01/06/2009"    | "01/06/2010"   | "01/01/2050"   | 0.11         || 0.12            | "07/2009"        | "05/2010"       | 120.00     | 1120.00
        /* Testes de cálculo considerando período de início e fim entre 01/01/2009 */
        1000.00        | "01/01/2009"    | null           | "01/12/2008"   | 0.00         || 0.00            | "02/2009"        | "12/2008"       | 0.00       | 1000.00
        1000.00        | "01/10/2008"    | null           | "01/02/2009"   | 0.01         || 0.04            | "11/2008"        | "01/2009"       | 40.00      | 1040.00
        1000.00        | "01/01/2008"    | null           | "01/12/2009"   | 0.10         || 0.22            | "02/2008"        | "11/2009"       | 220.00      | 1220.00
        1000.00        | "01/01/2009"    | "01/12/2008"   | "01/12/2050"   | 0.00         || 0.00            | "02/2009"        | "12/2008"       | 0.00       | 1000.00
        1000.00        | "01/10/2008"    | "01/02/2009"   | "01/02/2050"   | 0.01         || 0.04            | "11/2008"        | "01/2009"       | 40.00      | 1040.00
        1000.00        | "01/01/2008"    | "01/12/2009"   | "01/12/2050"   | 0.10         || 0.22            | "02/2008"        | "11/2009"       | 220.00      | 1220.00
        1000.00        | "01/12/2008"    | null           | "01/01/2009"   | 0.00         || 0.01            | "01/2009"        | "12/2008"       | 10.00      | 1010.00
    }
}
