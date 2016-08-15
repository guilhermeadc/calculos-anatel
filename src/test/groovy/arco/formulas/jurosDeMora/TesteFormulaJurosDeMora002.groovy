package arco.formulas.jurosDeMora

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaJurosDeMora002 extends Specification {

    def script = "JUROS_MORA_002.groovy"
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
        lancamento.dataCompetencia = data_competencia ? Date.parse("d/M/yyyy", data_competencia) : null
        lancamento.houveSuspencaoExigibilidade = houve_suspencao_exibilidade
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
        valor_original | data_vencimento | data_competencia | data_pagamento | houve_suspencao_exibilidade | data_atual     | indice_juros || indice_aplicado | mes_inicio_juros | mes_final_juros | juros_mora | valor_total
        1000.00        | "01/06/2016"    | "17/05/2016"     | null           | false                       | "01/05/2016"   | 0.00         || 0.00            | "07/2016"        | "04/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "17/05/2016"     | null           | false                       | "01/06/2016"   | 0.00         || 0.00            | "07/2016"        | "05/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "17/05/2016"     | null           | false                       | "02/06/2016"   | 0.00         || 0.00            | "07/2016"        | "05/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "17/05/2016"     | null           | false                       | "01/07/2016"   | 0.00         || 0.01            | "07/2016"        | "06/2016"       | 10.00      | 1010.00
        1000.00        | "01/06/2015"    | "17/05/2016"     | null           | false                       | "01/01/2016"   | 0.05         || 0.06            | "07/2015"        | "12/2015"       | 60.00      | 1060.00
        1000.00        | "01/06/2016"    | "17/05/2016"     | "01/05/2016"   | false                       | "01/01/2050"   | 0.00         || 0.00            | "07/2016"        | "04/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "17/05/2016"     | "01/06/2016"   | false                       | "01/01/2050"   | 0.00         || 0.00            | "07/2016"        | "05/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "17/05/2016"     | "02/06/2016"   | false                       | "01/01/2050"   | 0.00         || 0.00            | "07/2016"        | "05/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "17/05/2016"     | "01/07/2016"   | false                       | "01/01/2050"   | 0.00         || 0.01            | "07/2016"        | "06/2016"       | 10.00      | 1010.00
        1000.00        | "01/06/2015"    | "17/05/2016"     | "01/01/2016"   | false                       | "01/01/2050"   | 0.05         || 0.06            | "07/2015"        | "12/2015"       | 60.00      | 1060.00
        /* Execução do cálculo considerando data de competência anterior à mudança de regimento em 17/05/2012 */
        1000.00        | "01/06/2016"    | "16/05/2012"     | null           | false                       | "01/05/2016"   | 0.00         || null            | null             | null            | 0.00      | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | null           | false                       | "01/06/2016"   | 0.00         || null            | null             | null            | 0.00      | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | null           | false                       | "02/06/2016"   | 0.00         || null            | null             | null            | 0.00      | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | null           | false                       | "01/07/2016"   | 0.00         || null            | null             | null            | 0.00      | 1000.00
        1000.00        | "01/06/2015"    | "16/05/2012"     | null           | false                       | "01/01/2016"   | 0.05         || null            | null             | null            | 0.00      | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | "01/05/2016"   | false                       | "01/01/2050"   | 0.00         || null            | null             | null            | 0.00      | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | "01/06/2016"   | false                       | "01/01/2050"   | 0.00         || null            | null             | null            | 0.00      | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | "02/06/2016"   | false                       | "01/01/2050"   | 0.00         || null            | null             | null            | 0.00      | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | "01/07/2016"   | false                       | "01/01/2050"   | 0.00         || null            | null             | null            | 0.00      | 1000.00
        1000.00        | "01/06/2015"    | "16/05/2012"     | "01/01/2016"   | false                       | "01/01/2050"   | 0.05         || null            | null             | null            | 0.00      | 1000.00
        /* Execução do cálculo considerando data de competência anterior à mudança de regimento em 17/05/2012 */
        1000.00        | "01/06/2016"    | "17/05/2012"     | null           | false                       | "01/05/2016"   | 0.00         || 0.00            | "07/2016"        | "04/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "18/05/2012"     | null           | false                       | "01/06/2016"   | 0.00         || 0.00            | "07/2016"        | "05/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "19/05/2012"     | null           | false                       | "02/06/2016"   | 0.00         || 0.00            | "07/2016"        | "05/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "20/05/2012"     | null           | false                       | "01/07/2016"   | 0.00         || 0.01            | "07/2016"        | "06/2016"       | 10.00      | 1010.00
        1000.00        | "01/06/2015"    | "21/05/2012"     | null           | false                       | "01/01/2016"   | 0.05         || 0.06            | "07/2015"        | "12/2015"       | 60.00      | 1060.00
        1000.00        | "01/06/2016"    | "22/05/2012"     | "01/05/2016"   | false                       | "01/01/2050"   | 0.00         || 0.00            | "07/2016"        | "04/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "23/05/2012"     | "01/06/2016"   | false                       | "01/01/2050"   | 0.00         || 0.00            | "07/2016"        | "05/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "24/05/2012"     | "02/06/2016"   | false                       | "01/01/2050"   | 0.00         || 0.00            | "07/2016"        | "05/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "25/05/2012"     | "01/07/2016"   | false                       | "01/01/2050"   | 0.00         || 0.01            | "07/2016"        | "06/2016"       | 10.00      | 1010.00
        1000.00        | "01/06/2015"    | "26/05/2012"     | "01/01/2016"   | false                       | "01/01/2050"   | 0.05         || 0.06            | "07/2015"        | "12/2015"       | 60.00      | 1060.00

    }
}
