package arco

import junit.framework.AssertionFailedError
import spock.lang.Specification

/**
 * Created by guilhermeadc on 18/08/16.
 */
class CustomScriptTest extends Specification {

    class CustomScriptImpl extends arco.CustomScript {
        @Override
        Object run() { return null }
    }

    CustomScriptImpl customScript

    def setup () {
        customScript = new CustomScriptImpl()
    }

    def "Função MAXIMO deverá retornar a maior data válida entre às duas informadas"() {
        given: "Considerando duas datas válidas diferentes"
        Date dataMenor = new Date()
        Date dataMaior = new Date() + 1

        when: "Quando a função MINIMO for executada"
        def resultado1 = customScript.MAXIMO(dataMenor, dataMaior)
        def resultado2 = customScript.MAXIMO(dataMaior, dataMenor)

        then: "Então a maior data válida deverá ser retornada"
        resultado1 == dataMaior
        resultado2 == dataMaior
    }

    def "Função MAXIMO não pode receber ambos duas datas nulas para comparação"() {
        given: "Considerando que as duas datas passadas são nulas "
        Date data1 = null
        Date data2 = null

        when: "Quando a função MÁXIMO for executada"
        customScript.MAXIMO(data1, data2)

        then: "Então uma exceção do tipo AssertException deverá ser lançada"
        thrown(AssertionError)
    }

    def "Função MAXIMO deverá retornar a data válida no caso de um dos valores for nulo"() {
        given: "Considerando que apenas uma das datas comparadas é nula"
        Date dataNula = null
        Date dataValida = new Date()

        when: "Quando a função MÁXIMO for executada"
        def resultado1 = customScript.MINIMO(dataValida, dataNula)
        def resultado2 = customScript.MINIMO(dataNula, dataValida)

        then: "Então a data válida deverá ser retornada"
        resultado1 == dataValida
        resultado2 == dataValida
    }


    def "Função MINIMO deverá retornar a menor data válida entre às duas informadas"() {
        given: "Considerando duas datas válidas diferentes"
        Date dataMenor = new Date()
        Date dataMaior = new Date() + 1

        when: "Quando a função MINIMO for executada"
        def resultado1 = customScript.MINIMO(dataMenor, dataMaior)
        def resultado2 = customScript.MINIMO(dataMaior, dataMenor)

        then: "Então a menor data válida deverá ser retornada"
        resultado1 == dataMenor
        resultado2 == dataMenor
    }

    def "Função MÍNIMO não pode receber ambos duas datas nulas para comparação"() {
        given: "Considerando que as duas datas passadas são nulas "
        Date data1 = null
        Date data2 = null

        when: "Quando a função MINIMO for executada"
        customScript.MINIMO(data1, data2)

        then: "Então uma exceção do tipo AssertException deverá ser lançada"
        thrown(AssertionError)
    }

    def "Função MÍNIMO deverá retornar a data válida no caso de um dos valores for nulo"() {
        given: "Considerando que apenas uma das datas comparadas é nula"
        Date dataNula = null
        Date dataValida = new Date()

        when: "Quando a função MINIMO for executada"
        def resultado1 = customScript.MINIMO(dataValida, dataNula)
        def resultado2 = customScript.MINIMO(dataNula, dataValida)

        then: "Então a data válida deverá ser retornada"
        resultado1 == dataValida
        resultado2 == dataValida
    }

    def "Função DATA_ADICIONAR deverá retornar nova data considerando a quantidade de dias adicionados"() {
        given: "Considerando a data válida 01/06/2016"
        Date dataTest = new Date(2016, Calendar.JUNE, 01)

        when: "Quando for adicionado 5 dias"
        def resultado = customScript.DATA_ADICIONAR(dataTest, 05, "D")

        then: "Então a nova data deverá ser 06/06/2016"
        resultado == new Date(2016, Calendar.JUNE, 06)
    }

    def "Função DATA_ADICIONAR deverá retornar nova data considerando a quantidade de dias subtraídos"() {
        given: "Considerando a data válida 01/06/2016"
        Date dataTest = new Date(2016, Calendar.JUNE, 01)

        when: "Quando for subtraído 5 dias"
        def resultado = customScript.DATA_ADICIONAR(dataTest, -5, "D")

        then: "Então a nova data deverá ser 27/05/2016"
        resultado == new Date(2016, Calendar.MAY, 27)

    }

    def "Função DATA_ADICIONAR deverá retornar nova data considerando a quantidade de meses adicionados"() {
        given: "Considerando a data válida 01/06/2016"
        Date dataTest = new Date(2016, Calendar.JUNE, 01)

        when: "Quando for adicionado 5 meses"
        def resultado = customScript.DATA_ADICIONAR(dataTest, 05, "M")

        then: "Então a nova data deverá ser 01/11/2016"
        resultado == new Date(2016, Calendar.NOVEMBER, 01)
    }

    def "Função DATA_ADICIONAR deverá retornar nova data considerando a quantidade de meses subtraídos"() {
        given: "Considerando a data válida 01/06/2016"
        Date dataTest = new Date(2016, Calendar.JUNE, 01)

        when: "Quando for subtraído 5 meses"
        def resultado = customScript.DATA_ADICIONAR(dataTest, -5, "M")

        then: "Então a nova data deverá ser 01/01/2016"
        resultado == new Date(2016, Calendar.JANUARY, 01)
    }

    def "Função DATA_ADICIONAR deverá retornar nova data considerando a quantidade de meses adicionados acima de 12"() {
        given: "Considerando a data válida 01/06/2016"
        Date dataTest = new Date(2016, Calendar.JUNE, 01)

        when: "Quando for adicionado 15 meses"
        def resultado = customScript.DATA_ADICIONAR(dataTest, 15, "M")

        then: "Então a nova data deverá ser 01/09/2017"
        resultado == new Date(2017, Calendar.SEPTEMBER, 01)
    }

    def "Função DATA_ADICIONAR deverá retornar nova data considerando a quantidade de meses subtraídos acima de 12"() {
        given: "Considerando a data válida 01/06/2016"
        Date dataTest = new Date(2016, Calendar.JUNE, 01)

        when: "Quando for subtraído 15 meses"
        def resultado = customScript.DATA_ADICIONAR(dataTest, -15, "M")

        then: "Então a nova data deverá ser 01/03/2015"
        resultado == new Date(2015, Calendar.MARCH, 01)
    }

    def "Função DATA_ADICIONAR deverá retornar nova data considerando a quantidade de anos adicionados"() {
        given: "Considerando a data válida 01/06/2016"
        Date dataTest = new Date(2016, Calendar.JUNE, 01)

        when: "Quando for adicionado 5 anos"
        def resultado = customScript.DATA_ADICIONAR(dataTest, 05, "A")

        then: "Então a nova data deverá ser 01/11/2016"
        resultado == new Date(2021, Calendar.JUNE, 01)
    }

    def "Função DATA_ADICIONAR deverá retornar nova data considerando a quantidade de anos subtraídos"() {
        given: "Considerando a data válida 01/06/2016"
        Date dataTest = new Date(2016, Calendar.JUNE, 01)

        when: "Quando for subtraído 5 anos"
        def resultado = customScript.DATA_ADICIONAR(dataTest, -5, "A")

        then: "Então a nova data deverá ser 01/01/2011"
        resultado == new Date(2011, Calendar.JUNE, 01)
    }

}
