package arco

import groovy.transform.CompileStatic

import java.math.RoundingMode

/**
 * Created by guilhermeadc on 02/08/16.
 */

abstract class CustomScript extends groovy.lang.Script {

    //Variáveis disponíveis dentro  do contexto de execução das fórmulas
    def DATA_REFERENCIA = new Date();

    //Funções disponíveis dentro  do contexto de execução das fórmulas
    def MAXIMO(def valor1, def valor2) {
        assert valor1 != null || valor2 != null, "Parâmetros [valor1] e [valor2] não podem ser nulos"

        if(valor2 == null)       return valor1
        else if (valor1 == null) return valor2

        return valor1.compareTo(valor2) >= 0 ? valor1 : valor2
    }

    def MINIMO(def valor1, def valor2) {
        assert valor1 != null || valor2 != null, "Parâmetros [valor1] e [valor2] não podem ser nulos"

        if(valor2 == null)       return valor1
        else if (valor1 == null) return valor2

        return valor1.compareTo(valor2) < 0 ? valor1 : valor2
    }

    def SE(Boolean condicao, Object resultado1, Object resultado2) {
        return condicao ? resultado1 : resultado2
    }

    def VALIDACAO(Boolean condicao, String mensagem) {
        assert condicao, mensagem
    }

    def TRUNC(BigDecimal valor, int casasDecimais) {
        return valor.setScale(casasDecimais, BigDecimal.ROUND_DOWN)
    }

    def TRUNC(Double valor, int casasDecimais) {
        BigDecimal resultado = new BigDecimal(valor.toString())
        return resultado.setScale(casasDecimais, BigDecimal.ROUND_DOWN)
    }

    //Funções disponíveis dentro do contexto de execução das fórmulas
    def INDICE_ECONOMICO(String nomeIndice, Date dataInicial, Date dataFinal) {
        return 0
    }
}
