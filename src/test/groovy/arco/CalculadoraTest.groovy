package arco

import spock.lang.Specification

/**
 * Created by guilhermeadc on 02/08/16.
 */
class CalculadoraTest extends Specification {

    def calculadoraTest;

    void setup() {
        calculadoraTest = new Calculadora();
    }

    def "CalcularFormula"() {
        given:
        def script = "formula_basica.groovy"

        expect:
        calculadoraTest.executarFormula(script) != null
    }
}
