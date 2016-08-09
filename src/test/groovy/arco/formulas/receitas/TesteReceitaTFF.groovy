package arco.formulas.multaDeMora

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification
import com.xlson.groovycsv.CsvParser

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteReceitaTFF extends Specification {

    def script = ["formula_multa_mora_033_limite_20.groovy",
                  "formula_juros_mora_selic.groovy"]
    def calculadora = null
    def lancamento = null
    def parametros = null

    def setup() {
        calculadora = new Calculadora();
        lancamento = new Lancamento(valorOriginal: 1000.00, dataLancamento: new Date())
        parametros = [:]
    }

    def "Teste de calculo TFF"() {
        setup:
        lancamento.valorOriginal = new BigDecimal(valor_original)
        lancamento.dataVencimento = Date.parse("d/M/yyyy", data_vencimento)
        lancamento.dataPagamento = data_pagamento ? Date.parse("d/M/yyyy", data_pagamento) : null
        calculadora.DATA_REFERENCIA = Date.parse("d/M/yyyy", data_atual)
        calculadora.metaClass.INDICE_ECONOMICO = {String a, Date b, Date c -> return indice_juros - 0.01}

        expect:
        Lancamento resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.multaMora == multa_mora
        resultado.jurosMora == juros_mora
        resultado.multaOficio == multa_oficio
        resultado.valorTotal == valor_total
        //resultado.atualizacaoMonetaria == correcao

        where:
        valor_original | data_vencimento | data_pagamento | data_atual   | indice_juros | indice_multa || correcao    | multa_oficio | juros_mora | multa_mora | valor_total
        "1000.00"      | "01/06/2016"    | null           | "01/05/2016" | 0.00         | 0.00         || 0           | 0            | 0          | 0          | 1000.00
        "1000.00"      | "01/06/2016"    | null           | "01/06/2016" | 0.00         | 0.00         || 0           | 0            | 0          | 0          | 1000.00
        "1000.00"      | "01/06/2016"    | null           | "02/06/2016" | 0.00         | 0.00         || 0           | 0            | 0          | 3.30       | 1003.30
        "1000.00"      | "01/06/2016"    | null           | "01/07/2016" | 0.01         | 0.00         || 0           | 0            | 10.00      | 99.00      | 1109.00
        "1000.00"      | "01/06/2015"    | null           | "01/01/2016" | 0.06         | 0.00         || 0           | 0            | 60.00      | 200.00     | 1260.00
    }

    def "Teste de calculo TFF com dados CVS"() {
        setup:
        lancamento.valorOriginal = Double.parseDouble(valor_original)
        lancamento.dataVencimento = Date.parse("d/M/yyyy", data_vencimento)
        lancamento.dataPagamento = data_pagamento ? Date.parse("d/M/yyyy", data_pagamento) : null
        calculadora.DATA_REFERENCIA = Date.parse("d/M/yyyy", data_atual)
        calculadora.metaClass.INDICE_ECONOMICO = {String a, Date b, Date c -> return (Double.parseDouble(indice_juros ?: '0')/ 100) - 0.01}

        expect:
        Lancamento resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.multaMora == Double.parseDouble(multa_mora ?: '0')
        resultado.jurosMora == Double.parseDouble(juros_mora ?: '0')
        resultado.multaOficio == Double.parseDouble(multa_oficio ?: '0')
        resultado.valorTotal == Double.parseDouble(valor_total ?: '0')
        //resultado.atualizacaoMonetaria == correcao

        where:
        [valor_original,data_vencimento,data_pagamento,data_atual,indice_juros,indice_multa,correcao,multa_oficio,juros_mora,multa_mora,valor_total] << CsvParser.parseCsv(new FileReader("src/main/resources/dados_tff.csv"), separator: ';')

    }
}
