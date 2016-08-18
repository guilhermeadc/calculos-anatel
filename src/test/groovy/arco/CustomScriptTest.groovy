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
}
