package arco.formulas.multaDeMora

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaJurosDeMoraSelic extends Specification {

    def script = "formula_juros_mora_selic.groovy"
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
        lancamento.dataVencimento = Date.parse("d/MM/yyyy", data_vencimento)
        calculadora.DATA_REFERENCIA = Date.parse("d/MM/yyyy", data_atual)
        calculadora.metaClass.INDICE_ECONOMICO = {String a, Date b, Date c -> return indice_juros}

        expect:
        def resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.jurosMora == juros_mora
        resultado.valorTotal == valor_total
        //Validação dos calculos intermediários da formula
        calculadora.logUltimoCalculo["mes_inicio"] == mes_inicio_juros
        calculadora.logUltimoCalculo["mes_final"] == mes_final_juros

        where:
        valor_original | data_vencimento | data_atual    | indice_juros || mes_inicio_juros | mes_final_juros | juros_mora | valor_total
        1000.00        | "01/06/2016"    | "01/05/2016"  | null         || null             | null            | 0          | 1000.00
        1000.00        | "01/06/2016"    | "01/06/2016"  | null         || null             | null            | 0          | 1000.00
        1000.00        | "01/06/2016"    | "02/06/2016"  | 0.00         || "07/2016"        | "05/2016"       | 0          | 1000.00
        1000.00        | "01/06/2016"    | "01/07/2016"  | 0.01         || "07/2016"        | "06/2016"       | 10.00      | 1010.00
        1000.00        | "01/06/2015"    | "01/01/2016"  | 0.06         || "07/2015"        | "12/2015"       | 60.00      | 1060.00
          88.52        | "31/03/2014"    | "03/08/2016"  | 0.291483     || "04/2014"        | "07/2016"       | 25.80      | 88.52 + 25.80
    }
}
