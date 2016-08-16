package arco.formulas.correcaoMonetaria

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaAtualizacaoMonetaria001 extends Specification {

    def script = "ATUALIZACAO_MONETARIA_001.groovy"
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
        parametros["DATA_PUBLICAOCAO_DOU"] = data_publicacao_dou ? Date.parse("d/M/yyyy", data_publicacao_dou) : null
        //calculadora.DATA_REFERENCIA = data_atual ? Date.parse("d/MM/yyyy", data_atual) : new Date()
        calculadora.metaClass.INDICE_ECONOMICO = {String indice, Date b, Date c -> return (indice == "SELIC") ? indice_SELIC : indice_IGPDI}

        expect:
        def resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.atualizacaoMonetaria == atualizacao_monetaria
        resultado.valorTotal == valor_total

        where:
        valor_original | data_competencia | data_publicacao_dou | houve_suspencao_exibilidade | indice_SELIC | indice_IGPDI || atualizacao_monetaria | valor_total
        1000.00        | "01/01/2008"     | "01/02/2008"        | true                        | null         | 0.01         || 10.00                 | 1010.00
        1000.00        | "01/01/2008"     | "31/12/2008"        | true                        | null         | 0.02         || 20.00                 | 1020.00
        1000.00        | "01/01/2008"     | "01/01/2009"        | true                        | 0.01         | 0.02         || 30.00                 | 1030.00
        1000.00        | "02/12/2008"     | "31/12/2009"        | true                        | 0.02         | 0.02         || 40.00                 | 1040.00
        1000.00        | "03/12/2008"     | "31/12/2008"        | true                        | null         | null         || 0.00                  | 1000.00  //TODO: Avaliar como a legislação trata esta situação
        1000.00        | "03/12/2008"     | "01/12/2009"        | true                        | 0.05         | null         || 50.00                 | 1050.00
        1000.00        | "16/05/2012"     | "01/07/2012"        | true                        | 0.06         | null         || 60.00                 | 1060.00
        /* Execução do cálculo considerando data de competência posterior à mudança de regimento em 17/05/2012 */
        1000.00        | "17/05/2012"     | "01/07/2012"        | true                        | null         | null         || 0.00                 | 1000.00
        /* Execução do cálculo sem Suspensão de Exibilidade  */
        1000.00        | "02/12/2008"     | "31/12/2009"        | false                       | null         | null         || 0.00                 | 1000.00
        /* Execução do cálculo com Suspensão de Exibilidade sem publicação da decisão do Diário Oficial da União - DOU */
        1000.00        | "02/12/2008"     | null                | true                        | null         | null         || 0.00                  | 1000.00
    }
}
