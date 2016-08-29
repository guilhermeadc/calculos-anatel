package arco.formulas.correcaoMonetaria

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaAtualizacaoMonetaria002 extends Specification {

    def script = "ATUALIZACAO_MONETARIA_002.groovy"
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
        lancamento.dataCompetencia = data_competencia ? Date.parse("d/M/yyyy", data_competencia) : null
        lancamento.houveSuspencaoExigibilidade = houve_suspencao_exibilidade
        parametros["DATA_CONSTITUICAO_MULTA"] = data_constituicao ? Date.parse("d/M/yyyy", data_constituicao) : null
        parametros["DATA_INTIMACAO_MULTA"] = data_competencia ? Date.parse("d/M/yyyy", data_competencia) : null
        calculadora.metaClass.INDICE_ECONOMICO = {String indice, Date b, Date c -> return indice_SELIC}

        expect:
        def resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.atualizacaoMonetaria == atualizacao_monetaria
        resultado.valorTotal == valor_total

        where:
        valor_original | data_competencia | data_constituicao   | houve_suspencao_exibilidade | indice_SELIC || atualizacao_monetaria | valor_total
        1000.00        | "01/01/2016"     | "01/01/2016"        | true                        | 0.00         || 0.00                  | 1000.00
        1000.00        | "01/01/2016"     | "01/02/2016"        | true                        | 0.01         || 10.00                 | 1010.00
        1000.00        | "02/12/2016"     | "31/12/2016"        | true                        | 0.02         || 20.00                 | 1020.00
        1000.00        | "17/05/2012"     | "01/07/2012"        | true                        | 0.03         || 30.00                 | 1030.00
        /* Execução do cálculo considerando data de competência anterior à mudança de regimento em 17/05/2012 */
        1000.00        | "16/05/2012"     | "01/07/2012"        | true                        | null         || 0.00                  | 1000.00
        /* Execução do cálculo sem Suspensão de Exibilidade  */
        1000.00        | "01/01/2016"     | "31/12/2016"        | false                       | null         || 0.00                  | 1000.00
        /* Execução do cálculo com Suspensão de Exibilidade sem publicação da decisão do Diário Oficial da União - DOU */
        1000.00        | "01/01/2016"     | null                | true                        | null         || 0.00                  | 1000.00
    }
}
