if(lancamento.dataVencimento < DATA_REFERENCIA) {
    def mesInicio = lancamento.dataVencimento.copyWith(date: 01, month: lancamento.dataVencimento[Calendar.MONTH] + 1)
    def mesFinal = DATA_REFERENCIA.copyWith(date: 01, month: DATA_REFERENCIA[Calendar.MONTH] - 1)
    def indiceAcumulado = INDICE_ECONOMICO("SELIC", mesInicio, mesFinal)
    lancamento.jurosMora = TRUNC((indiceAcumulado * lancamento.valorOriginal),2)

    //Log: Utilizado apenas para validação do cálculo
    LOG_CALCULO["mes_inicio"] = mesInicio.format("MM/yyyy")
    LOG_CALCULO["mes_final"] = mesFinal.format("MM/yyyy")
}
