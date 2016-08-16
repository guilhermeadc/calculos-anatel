package arco.formulas.jurosDeMora

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaJurosDeMora005 extends Specification {

    def script = "JUROS_MORA_005.groovy"
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
        lancamento.dataPagamento = data_pagamento ? Date.parse("d/MM/yyyy", data_pagamento) : null
        lancamento.dataCompetencia = data_competencia ? Date.parse("d/M/yyyy", data_competencia) : null
        lancamento.houveSuspencaoExigibilidade = houve_suspencao_exibilidade
        parametros["DATA_PUBLICAOCAO_DOU"] = data_publicacao_dou ? Date.parse("d/M/yyyy", data_publicacao_dou) : null
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
        valor_original | data_publicacao_dou | data_competencia | data_pagamento | houve_suspencao_exibilidade | data_atual     | indice_juros || indice_aplicado | mes_inicio_juros | mes_final_juros | juros_mora | valor_total
        1000.00        | null                | "16/05/2012"     | null           | true                        | "01/05/2016"   | 0.00         || null            | null             | null            | 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | null           | true                        | "01/05/2016"   | 0.00         || 0.00            | "08/2016"        | "04/2016"       | 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | null           | true                        | "01/06/2016"   | 0.00         || 0.00            | "08/2016"        | "05/2016"       | 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | null           | true                        | "01/07/2016"   | 0.00         || 0.00            | "08/2016"        | "06/2016"       | 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | null           | true                        | "01/08/2016"   | 0.00         || 0.01            | "08/2016"        | "07/2016"       | 10.00      | 1010.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | null           | true                        | "01/09/2016"   | 0.05         || 0.06            | "08/2016"        | "08/2016"       | 60.00      | 1060.00
        /* Testes considerando a data de pagamento (quitação) da receita */
        1000.00        | null                | "16/05/2012"     | "01/05/2016"   | true                        | "01/01/2050"   | 0.00         || null            | null             | null            | 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | "01/05/2016"   | true                        | "01/01/2050"   | 0.00         || 0.00            | "08/2016"        | "04/2016"       | 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | "01/06/2016"   | true                        | "01/01/2050"   | 0.00         || 0.00            | "08/2016"        | "05/2016"       | 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | "01/07/2016"   | true                        | "01/01/2050"   | 0.00         || 0.00            | "08/2016"        | "06/2016"       | 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | "01/08/2016"   | true                        | "01/01/2050"   | 0.00         || 0.01            | "08/2016"        | "07/2016"       | 10.00      | 1010.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | "01/09/2016"   | true                        | "01/01/2050"   | 0.05         || 0.06            | "08/2016"        | "08/2016"       | 60.00      | 1060.00
        /* Execução do cálculo considerando data de competência posterior à mudança de regimento em 17/05/2012 */
        1000.00        | "01/06/2016"        | "17/05/2012"     | null           | true                        | "01/08/2016"   | 0.00         || null            | null             | null            | 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "17/05/2012"     | null           | true                        | "01/09/2016"   | 0.05         || null            | null             | null            | 0.00       | 1000.00
        /* Execução do cálculo sem Suspensão de Exibilidade  */
        1000.00        | "01/06/2016"        | "16/05/2012"     | null           | false                       | "01/08/2016"   | 0.00         || null            | null             | null            | 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | null           | false                       | "01/09/2016"   | 0.05         || null            | null             | null            | 0.00       | 1000.00
        /* Execução do cálculo com Suspensão de Exibilidade sem publicação da decisão do Diário Oficial da União - DOU */
        1000.00        | null                | "16/05/2012"     | null           | true                        | "01/08/2016"   | 0.00         || null            | null             | null            | 0.00       | 1000.00
        1000.00        | null                | "16/05/2012"     | null           | true                        | "01/09/2016"   | 0.05         || null            | null             | null            | 0.00       | 1000.00
    }
}
