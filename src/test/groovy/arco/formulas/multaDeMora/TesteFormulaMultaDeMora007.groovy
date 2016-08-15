package arco.formulas.multaDeMora

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaMultaDeMora007 extends Specification {

    def script = "MULTA_MORA_007.groovy"
    def calculadora = null
    def lancamento = null
    def parametros = null

    def setup() {
        calculadora = new Calculadora();
        lancamento = new Lancamento(valorOriginal: 1000.00, dataLancamento: new Date())
        parametros = [:]
    }

    def "Teste de Cálculo de Multa de Mora para Multas com Suspenção de Exigibilidade anteriores à 17/05/2016"() {
        setup:
        calculadora.DATA_REFERENCIA = data_atual ? Date.parse("d/M/yyyy", data_atual) : null
        lancamento.dataPagamento = data_pagamento ? Date.parse("d/M/yyyy", data_pagamento) : null
        lancamento.dataCompetencia = data_competencia ? Date.parse("d/M/yyyy", data_competencia) : null
        lancamento.houveSuspencaoExigibilidade = houve_suspencao_exibilidade
        lancamento.valorOriginal = valor_original
        parametros["DATA_PUBLICAOCAO_DOU"] = data_publicacao_dou ? Date.parse("d/M/yyyy", data_publicacao_dou) : null

        expect:
        def resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.multaMora == multa_mora
        resultado.valorTotal == valor_total

        where:
        valor_original | data_publicacao_dou | data_competencia | data_pagamento | houve_suspencao_exibilidade | data_atual   || multa_mora | valor_total
        1000.00        | null                | "16/05/2012"     | null           | true                        | "01/06/2016" || 0          | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | null           | true                        | "01/05/2016" || 0          | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | null           | true                        | "01/06/2016" || 0          | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | null           | true                        | "30/06/2016" || 0          | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | null           | true                        | "02/07/2016" || 3.30       | 1003.30
        1000.00        | "01/06/2016"        | "16/05/2012"     | null           | true                        | "31/07/2016" || 99.00      | 1099.00
        1000.00        | "01/06/2015"        | "16/05/2012"     | null           | true                        | "01/01/2016" || 200.00     | 1200.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | "01/05/2016"   | true                        | "01/01/2018" || 0          | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | "01/06/2016"   | true                        | "01/01/2018" || 0          | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | "30/06/2016"   | true                        | "01/01/2018" || 0          | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | "02/07/2016"   | true                        | "01/01/2018" || 3.30       | 1003.30
        1000.00        | "01/06/2016"        | "16/05/2012"     | "31/07/2016"   | true                        | "01/01/2018" || 99.00      | 1099.00
        1000.00        | "01/06/2015"        | "16/05/2012"     | "01/01/2016"   | true                        | "01/01/2018" || 200.00     | 1200.00
        /* Execução do cálculo considerando data de competência anterior à mudança de regimento em 17/05/2012 */
        1000.00        | null                | "17/05/2012"     | null           | true                        | "01/06/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "17/05/2012"     | null           | true                        | "31/07/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "17/05/2012"     | "31/07/2016"   | true                        | "01/01/2018" || 0.00       | 1000.00
        /* Execução do cálculo sem suspenção de exibilidade */
        1000.00        | null                | "16/05/2012"     | null           | false                       | "01/06/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | null           | false                       | "31/07/2016" || 0.00       | 1000.00
        1000.00        | "01/06/2016"        | "16/05/2012"     | "31/07/2016"   | false                       | "01/01/2018" || 0.00       | 1000.00
        /* Execução do cálculo considerando os limites de multa de mora em 10% ou 20%, baseado na data DOU + 30 superior e inferior à 03/12/2008 */
        1000.00        | "03/11/2008"        | "16/05/2012"     | null           | true                        | "01/01/2016" || 200.00     | 1200.00
        1000.00        | "03/11/2008"        | "16/05/2012"     | "01/01/2016"   | true                        | "01/01/2018" || 200.00     | 1200.00
        1000.00        | "02/11/2008"        | "16/05/2012"     | null           | true                        | "01/01/2016" || 100.00     | 1100.00
        1000.00        | "02/11/2008"        | "16/05/2012"     | "01/01/2016"   | true                        | "01/01/2018" || 100.00     | 1100.00
    }
}
