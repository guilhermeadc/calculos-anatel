package arco
/**
 * Created by guilhermeadc on 10/05/16.
 */

class Lancamento {

    boolean houveSuspencaoExigibilidade
    Date dataLancamento = null
    Date dataVencimento = null
    Date dataPagamento = null

    BigDecimal valorOriginal = BigDecimal.ZERO
    BigDecimal jurosMora = BigDecimal.ZERO
    BigDecimal multaMora = BigDecimal.ZERO
    BigDecimal multaOficio = BigDecimal.ZERO
    BigDecimal atualizacaoMonetaria = BigDecimal.ZERO

    BigDecimal getValorTotal(){
        return valorOriginal + jurosMora + multaMora + multaOficio + atualizacaoMonetaria
    }

    BigDecimal getValorAtualizado(){
        return valorOriginal + atualizacaoMonetaria
    }

}