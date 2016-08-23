package arco.formulas.correcaoMonetaria

import arco.Calculadora
import arco.Lancamento
import spock.lang.Specification

/**
 * Created by guilhermeadc on 10/05/16.
 */
class TesteFormulaAtualizacaoMonetaria005 extends Specification {

    def script = "ATUALIZACAO_MONETARIA_005.groovy"
    def calculadora = null
    def lancamento = null
    def parametros = null

    def setup() {
        calculadora = new Calculadora()
        lancamento = new Lancamento(valorOriginal: 1000.00, dataLancamento: new Date())
        parametros = [:]
    }

    def "Teste de calculo de atualização monetária para Lote A e B, do Edital de Licitação 2/2015-SOR/SPR/CD-ANATEL"() {
        setup:
        lancamento.valorOriginal = valor_original
        lancamento.dataPagamento = data_pagamento ? Date.parse("d/MM/yyyy", data_pagamento) : null
        parametros["DATA_ENTREGA_DOCUMENTACAO"] = data_documentacao  ? Date.parse("d/MM/yyyy", data_documentacao) : null
        parametros["DATA_PUBLICACAO_AUTORIZACAO"] = data_publicacao  ? Date.parse("d/MM/yyyy", data_publicacao) : null
        calculadora.DATA_REFERENCIA = data_atual ? Date.parse("d/MM/yyyy", data_atual) : new Date()
        calculadora.metaClass.INDICE_ECONOMICO = {String a, Date b, Date c -> return indice_juros}

        expect:
        def resultado = calculadora.executarFormula(script, lancamento, parametros)
        resultado.atualizacaoMonetaria == atualizacao
        resultado.valorTotal == valor_total

        where:
        valor_original | data_pagamento | data_documentacao | data_publicacao | data_atual     | indice_juros || atualizacao | valor_total
        1000.00        | null           | "01/06/2016"      | "01/06/2016"    | "01/05/2016"   | 0.00         || 0.00        | 1000.00
        1000.00        | null           | "01/06/2016"      | "01/06/2016"    | "01/06/2016"   | 0.00         || 0.00        | 1000.00
        1000.00        | null           | "01/06/2016"      | "01/06/2016"    | "02/06/2016"   | 0.00         || 0.00        | 1000.00
        1000.00        | null           | "01/06/2016"      | "01/06/2016"    | "01/07/2016"   | 0.01         || 10.00       | 1010.00
        1000.00        | null           | "01/01/2015"      | "01/06/2015"    | "01/01/2016"   | 0.02         || 20.00       | 1020.00
        1000.00        | null           | "01/01/2015"      | "01/01/2016"    | "01/06/2016"   | 0.02         || 71.00       | 1071.00
        1000.00        | "01/05/2016"   | "01/06/2016"      | "01/06/2016"    | "01/05/2050"   | 0.00         || 0.00        | 1000.00
        1000.00        | "01/06/2016"   | "01/06/2016"      | "01/06/2016"    | "01/06/2050"   | 0.00         || 0.00        | 1000.00
        1000.00        | "02/06/2016"   | "01/06/2016"      | "01/06/2016"    | "02/06/2050"   | 0.00         || 0.00        | 1000.00
        1000.00        | "01/07/2016"   | "01/06/2016"      | "01/06/2016"    | "01/07/2050"   | 0.01         || 10.00       | 1010.00
        1000.00        | "01/01/2016"   | "01/01/2015"      | "01/06/2015"    | "01/01/2050"   | 0.02         || 20.00       | 1020.00
        1000.00        | "01/06/2016"   | "01/01/2015"      | "01/01/2016"    | "01/06/2050"   | 0.02         || 71.00       | 1071.00

    }

    def "Teste de calculo de atualização monetária sem informação da data de entrega da documentação"() {
        when: "Quando a fórmula é executada com lancamento sem data de entrega da documentação"
        calculadora.executarFormula(script, lancamento, ["DATA_ENTREGA_DOCUMENTACAO": null, "DATA_PUBLICACAO_AUTORIZACAO": new Date()])

        then: "Então é esperado uma exceção"
        thrown(AssertionError)
    }

    def "Teste de calculo de atualização monetária sem informação da data de publicação da autorização"() {
        when: "Quando a fórmula é executada com lancamento sem data de entrega da documentação"
        calculadora.executarFormula(script, lancamento, ["DATA_ENTREGA_DOCUMENTACAO": new Date(), "DATA_PUBLICACAO_AUTORIZACAO": null])

        then: "Então é esperado uma exceção"
        thrown(AssertionError)
    }
}
