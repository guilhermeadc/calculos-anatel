package arco

import java.math.RoundingMode

/**
 * Created by guilhermeadc on 02/08/16.
 */
abstract class CustomScript extends groovy.lang.Script {

    //Variáveis disponíveis dentro  do contexto de execução das fórmulas
    def DATA_REFERENCIA = new Date();

    //Funções disponíveis dentro  do contexto de execução das fórmulas
    def MAXIMO(def valor1, def valor2) {
        return Math.max(valor1, valor2);
    }

    def MAXIMO(Date data1, Date data2) {
        assert data1 != null, "Parâmetro [data1] não pode ser nulo"
        assert data2 != null, "Parâmetro [data2] não pode ser nulo"

        return data1.compareTo(data2) > 0 ? data1 : data2
    }

    def MINIMO(def valor1, def valor2){
        return Math.min(valor1, valor2);
    }

    def MINIMO(Date data1, Date data2) {
        assert data1 != null, "Parâmetro [data1] não pode ser nulo"
        assert data2 != null, "Parâmetro [data2] não pode ser nulo"

        return data1.compareTo(data2) < 0 ? data1 : data2
    }



    def SE(Boolean condicao, Object resultado1, Object resultado2){
        return condicao ? resultado1 : resultado2
    }

    def VALIDACAO(Boolean condicao, String mensagem){
        assert condicao, mensagem
    }

    def TRUNC(BigDecimal valor, int casasDecimais){
        return valor.setScale(casasDecimais, BigDecimal.ROUND_DOWN)
    }

    def TRUNC(Double valor, int casasDecimais){
        BigDecimal resultado = new BigDecimal(valor.toString())
        return resultado.setScale(casasDecimais, BigDecimal.ROUND_DOWN)
    }

    //Funções disponíveis dentro do contexto de execução das fórmulas
    def INDICE_ECONOMICO(String nomeIndice, Date dataInicial, Date dataFinal){
        return 0
    }
}
