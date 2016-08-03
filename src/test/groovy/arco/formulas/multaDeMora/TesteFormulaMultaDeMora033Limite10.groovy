package arco.formulas.multaDeMora

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaMultaDeMora033Limite10 extends Specification {

    def script = "formula_multa_mora_033_limite_10.groovy"
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
        calculadora.DATA_REFERENCIA = Date.parse("d/M/yyyy", data_atual)
        lancamento.valorOriginal = valor_original
        lancamento.dataVencimento = Date.parse("d/M/yyyy", data_vencimento)

        expect:
        def resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.multaMora == multa_mora
        resultado.valorTotal == valor_total

        where:
        valor_original | data_vencimento | data_atual   || multa_mora | valor_total
        1000.00        | "01/06/2016"    | "01/05/2016" || 0          | 1000.00      //Data atual inferior ao dia de vencimento (0 dias de atraso)
        1000.00        | "01/06/2016"    | "01/06/2016" || 0          | 1000.00      //Data atual igual ao dia de vencimento (0 dias de atraso)
        1000.00        | "01/06/2016"    | "02/06/2016" || 3.30       | 1003.30      //Data atual posterior a data de vencimento (1 dias de atraso)
        1000.00        | "01/06/2016"    | "01/07/2016" || 99.00      | 1099.00      //Data atual 1 mês posterior a data de vencimento (30 dias de atraso)
        1000.00        | "01/06/2015"    | "01/01/2016" || 200.00     | 1200.00      //Data atual 6 mês posterior a data de vencimento. Multa limitada à 20%
    }

    def "Teste de calculo de multa de mora sem data de vencimento"() {
        when: "Quando a fórmula é executada com lancamento sem data de vencimento"
        calculadora.executarFormula(script, lancamento, parametros)

        then:
        thrown(AssertionError)
    }
}
