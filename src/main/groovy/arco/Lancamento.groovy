package arco
/**
 * Created by guilhermeadc on 10/05/16.
 */

class Lancamento {

    boolean suspencaoExigibilidade
    Date dataLancamento
    Date dataVencimento

    double valorOriginal = BigDecimal.ZERO
    double jurosMora = BigDecimal.ZERO
    double multaMora = BigDecimal.ZERO
    double multaOficio = BigDecimal.ZERO
    double atualizacaoMonetaria = BigDecimal.ZERO

    double getValorTotal(){
        return valorOriginal + jurosMora + multaMora + multaOficio + atualizacaoMonetaria
    }

    double getValorAtualizado(){
        return valorOriginal + atualizacaoMonetaria
    }

}