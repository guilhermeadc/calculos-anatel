package arco.formulas.jurosDeMora

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaJurosDeMora007 extends Specification {

    def script = "JUROS_MORA_007.groovy"
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
        //lancamento.dataVencimento = data_vencimento ? Date.parse("d/MM/yyyy", data_vencimento) : null
        lancamento.dataPagamento = data_pagamento ? Date.parse("d/MM/yyyy", data_pagamento) : null
        parametros["DATA_RECEBIMENTO_COMUNICADO_COBRANCA"] = data_comunicado ? Date.parse("d/MM/yyyy", data_comunicado) : null
        calculadora.DATA_REFERENCIA = data_atual ? Date.parse("d/MM/yyyy", data_atual) : new Date()
        calculadora.metaClass.INDICE_ECONOMICO = {String a, Date b, Date c -> return indice_juros}

        expect:
        def resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.jurosMora == juros_mora
        resultado.valorTotal == valor_total
        //Validação dos calculos intermediários da formula
        calculadora.logUltimoCalculo["mes_inicio"] == mes_inicio_juros
        calculadora.logUltimoCalculo["mes_final"] == mes_final_juros
        calculadora.logUltimoCalculo["indice_aplicado"] == indice_aplicado

        where:
        valor_original | data_comunicado | data_pagamento | data_atual     | indice_juros || indice_aplicado | mes_inicio_juros | mes_final_juros | juros_mora | valor_total
        1000.00        | "01/06/2016"    | null           | "01/05/2016"   | 0.00         || 0.00            | "07/2016"        | "04/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | null           | "01/06/2016"   | 0.00         || 0.00            | "07/2016"        | "05/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | null           | "02/06/2016"   | 0.00         || 0.00            | "07/2016"        | "05/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | null           | "01/07/2016"   | 0.00         || 0.01            | "07/2016"        | "06/2016"       | 10.00      | 1010.00
        1000.00        | "01/06/2015"    | null           | "01/01/2016"   | 0.05         || 0.06            | "07/2015"        | "12/2015"       | 60.00      | 1060.00
        1000.00        | "01/06/2016"    | "01/05/2016"   | "01/01/2050"   | 0.00         || 0.00            | "07/2016"        | "04/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "01/06/2016"   | "01/01/2050"   | 0.00         || 0.00            | "07/2016"        | "05/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "02/06/2016"   | "01/01/2050"   | 0.00         || 0.00            | "07/2016"        | "05/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "01/07/2016"   | "01/01/2050"   | 0.00         || 0.01            | "07/2016"        | "06/2016"       | 10.00      | 1010.00
        1000.00        | "01/06/2015"    | "01/01/2016"   | "01/01/2050"   | 0.05         || 0.06            | "07/2015"        | "12/2015"       | 60.00      | 1060.00
    }
}
