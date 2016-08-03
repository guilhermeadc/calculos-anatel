//VALIDACAO(lancamento.dataVencimento != null, "Data de vencimento não pode ser nula.")
//if(lancamento.dataVencimento < DATA_REFERENCIA) {
//    def indiceAcumulado = INDICE_ECONOMICO("SELIC", lancamento.dataVencimento, DATA_REFERENCIA)
//    lancamento.jurosMora = lancamento.valorOriginal * indiceAcumulado
//}

if(lancamento.dataVencimento < DATA_REFERENCIA) {
    def mesInicio = lancamento.dataVencimento.copyWith(date: 01, month: lancamento.dataVencimento[Calendar.MONTH] + 1)
    def mesFinal = DATA_REFERENCIA.copyWith(date: 01, month: DATA_REFERENCIA[Calendar.MONTH] - 1)
    def indiceAcumulado = INDICE_ECONOMICO("SELIC", mesInicio, mesFinal)
    lancamento.jurosMora = indiceAcumulado * lancamento.valorOriginal

    //Log: Utilizado apenas para validação do cálculo
    LOG_CALCULO["mes_inicio"] = mesInicio.format("MM/yyyy")
    LOG_CALCULO["mes_final"] = mesFinal.format("MM/yyyy")
}
