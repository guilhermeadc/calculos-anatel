package arco

import groovy.transform.CompileStatic
import org.omg.CORBA.portable.ApplicationException

import java.math.RoundingMode
import java.text.DateFormat
import java.text.FieldPosition
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period

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

    def DATA(String data) {
        assert data != null, "Parâmetro [data] não pode ser nulo"

        DateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy")
        return dateFormat.parse(data)
    }

    def DATA_ADICIONAR(Date dataReferencia, int quantidade, String unidade = "D") {
        assert dataReferencia != null, "Parâmetro [dataReferencia] não pode ser nulo"
        assert ["A", "M", "D"].contains(unidade), "Parâmetro [unidade] deve possuir um dos seguntes valores: 'D' (dia), 'M' (mẽs) ou 'A' (ano)"

        def dia = dataReferencia[Calendar.DATE]
        def mes = dataReferencia[Calendar.MONTH]
        def ano = dataReferencia[Calendar.YEAR]

        switch (unidade){
            case "D": return dataReferencia.copyWith(date: dia + quantidade)
            case "M": return dataReferencia.copyWith(month: mes + quantidade)
            case "A": return dataReferencia.copyWith(year: ano + quantidade)
            default: throw new RuntimeException("Unidade de medida não pode ser determinada")
        }
    }

    def DATADIF(String dataInicial, String dataFinal, String unidade = "D") {
        Date inicio = DATA(dataInicial)
        Date fim = DATA(dataFinal)
        return DATADIF(inicio, fim, unidade)
    }

    def DATADIF(Date dataInicial, Date dataFinal, String unidade = "D") {

        assert dataInicial != null, "Parâmetro [dataInicial] não pode ser nulo"
        assert dataFinal != null, "Parâmetro [dataFinal] não pode ser nulo"
        assert unidade != null, "Parâmetro [unidade] não pode ser nulo"
        assert ["A", "M", "D"].contains(unidade), "Parâmetro [unidade] deve possuir um dos seguntes valores: 'D' (dia), 'M' (mẽs) ou 'A' (ano)"

        LocalDate localDateInicio = dataInicial.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
        LocalDate localDateFim = dataFinal.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
        Period periodo = Period.between(localDateInicio, localDateFim)

        switch (unidade){
            case "D": return periodo.getDays()
            case "M": return periodo.getMonths()
            case "A": return periodo.getYears()
            default: throw new RuntimeException("Unidade de medida não pode ser determinada")
        }
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
