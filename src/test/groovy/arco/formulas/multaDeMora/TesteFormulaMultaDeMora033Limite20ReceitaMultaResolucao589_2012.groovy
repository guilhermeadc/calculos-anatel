package arco.formulas.multaDeMora

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaMultaDeMora033Limite20ReceitaMultaResolucao589_2012 extends Specification {

    def script = "formula_multa_mora_receita_multa_resolucao_589_2012.groovy"
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

        expect:
        def resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.multaMora == multa_mora
        resultado.valorTotal == valor_total

        where:
        valor_original | data_vencimento | data_competencia | data_pagamento | houve_suspencao_exibilidade  | data_atual   || multa_mora | valor_total
        1000.00        | "01/06/2016"    | "17/05/2012"     | null           | false                       | "01/05/2016" || 0          | 1000.00
        1000.00        | "01/06/2016"    | "18/05/2012"     | null           | false                       | "01/06/2016" || 0          | 1000.00
        1000.00        | "01/06/2016"    | "19/05/2012"     | null           | false                       | "02/06/2016" || 3.30       | 1003.30
        1000.00        | "01/06/2016"    | "20/05/2012"     | null           | false                       | "01/07/2016" || 99.00      | 1099.00
        1000.00        | "01/06/2015"    | "21/05/2012"     | null           | false                       | "01/01/2016" || 200.00     | 1200.00
        1000.00        | "01/06/2016"    | "22/05/2012"     | "01/05/2016"   | false                       | "01/01/2050" || 0          | 1000.00
        1000.00        | "01/06/2016"    | "23/05/2012"     | "01/06/2016"   | false                       | "01/01/2050" || 0          | 1000.00
        1000.00        | "01/06/2016"    | "24/05/2012"     | "02/06/2016"   | false                       | "01/01/2050" || 3.30       | 1003.30
        1000.00        | "01/06/2016"    | "25/05/2012"     | "01/07/2016"   | false                       | "01/01/2050" || 99.00      | 1099.00
        1000.00        | "01/06/2015"    | "26/05/2012"     | "01/01/2016"   | false                       | "01/01/2050" || 200.00     | 1200.00
        /* Execução do cálculo considerando data de competência anterior à mudança de regimento em 17/05/2012 */
        1000.00        | "01/06/2016"    | "16/05/2012"     | null           | false                       | "01/05/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | null           | false                       | "01/06/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | null           | false                       | "02/06/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | null           | true                        | "01/07/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2015"    | "16/05/2012"     | null           | true                        | "01/01/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | "01/05/2016"   | true                        | "01/01/2050" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | "01/06/2016"   | true                        | "01/01/2050" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | "02/06/2016"   | true                        | "01/01/2050" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "16/05/2012"     | "01/07/2016"   | true                        | "01/01/2050" || 0.00       | 1000.00
        1000.00        | "01/06/2015"    | "16/05/2012"     | "01/01/2016"   | true                        | "01/01/2050" || 0.00       | 1000.00
        /* Execução do cálculo considerando data de competência anterior à mudança de regimento em 17/05/2012 */
        1000.00        | "01/06/2016"    | "17/05/2012"     | null           | true                        | "01/05/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "18/05/2012"     | null           | true                        | "01/06/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "19/05/2012"     | null           | true                        | "02/06/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "20/05/2012"     | null           | true                        | "01/07/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2015"    | "21/05/2012"     | null           | true                        | "01/01/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "22/05/2012"     | "01/05/2016"   | true                        | "01/01/2050" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "23/05/2012"     | "01/06/2016"   | true                        | "01/01/2050" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "24/05/2012"     | "02/06/2016"   | true                        | "01/01/2050" || 0.00       | 1000.00
        1000.00        | "01/06/2016"    | "25/05/2012"     | "01/07/2016"   | true                        | "01/01/2050" || 0.00       | 1000.00
        1000.00        | "01/06/2015"    | "26/05/2012"     | "01/01/2016"   | true                        | "01/01/2050" || 0.00       | 1000.00



    }

    def "Teste de calculo de multa de mora sem data de vencimento"() {
        when: "Quando a fórmula é executada com lancamento sem data de vencimento"
        calculadora.executarFormula(script, lancamento, parametros)

        then:
        thrown(AssertionError)
    }
}
