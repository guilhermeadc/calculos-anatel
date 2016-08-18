package formulas.jurosDeMora

import java.time.Duration
import java.time.Period

/***********************************************************************************************************
 * Cálculo de Juros de Mora para Fust - Multa de Ofício
 *
 * Correspondentes a 1% (um por cento) ao mês até dezembro de 2008, substituídos, a partir de janeiro de 2009,
 * pela taxa referencial do Sistema Especial de Liquidação e Custódia – SELIC, acumulada mensalmente, a partir
 * do mês subsequente ao vencimento do prazo e de 1% (um por cento) no mês do pagamento
 **********************************************************************************************************/

VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo.')

def indiceAcumulado = 0.00
def dataLimiteMudancaRegimento = Date.parse("d/M/yyyy", "31/12/2008");
def dataInicioCobranca = lancamento.dataVencimento.copyWith(date: 01, month: lancamento.dataVencimento[Calendar.MONTH] + 1)
def dataFinalCobranca = lancamento.dataPagamento ?: DATA_REFERENCIA

//Correspondentes a 1% (um por cento) ao mês até dezembro de 2008
if(dataInicioCobranca <= dataLimiteMudancaRegimento) {
    def dataInicioCobranca1Porcento = MINIMO(dataInicioCobranca, dataLimiteMudancaRegimento)
    def dataFinalCobranca1Porcento = MINIMO(dataFinalCobranca, dataLimiteMudancaRegimento)

    def localDateInicio = dataInicioCobranca1Porcento.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
    def localDateFim = dataFinalCobranca1Porcento.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
    Period periodoAnterior2009 = Period.between(localDateInicio, localDateFim)
    indiceAcumulado = (dataInicioCobranca1Porcento <= dataFinalCobranca) ? MAXIMO((periodoAnterior2009.getMonths() + 1) / 100, 0.00) : 0.00
}

// A partir de janeiro de 2009, atualiza pela taxa referencial do SELIC, acumulada mensalmente, a partir do mês subsequente ao vencimento do prazo
def dataInicioCobrancaSelic = MAXIMO(dataInicioCobranca, dataLimiteMudancaRegimento + 1)
def dataFinalCobrancaSelic = MAXIMO(dataFinalCobranca, dataLimiteMudancaRegimento + 1)
dataFinalCobrancaSelic = dataFinalCobrancaSelic.copyWith(date: 01, month: dataFinalCobrancaSelic[Calendar.MONTH]) - 1

// ... e de um por cento no mês de pagamento
indiceAcumulado += (dataInicioCobrancaSelic <= dataFinalCobranca) ? INDICE_ECONOMICO("SELIC", dataInicioCobrancaSelic, dataFinalCobrancaSelic) + 0.01 : 0.00

// Calculo do juros de mora considerando apenas 2 casas decimais
lancamento.jurosMora = TRUNC(lancamento.valorOriginal * indiceAcumulado, 2)

//Log: Utilizado apenas para validação do cálculo
LOG_CALCULO["mes_inicio"] = dataInicioCobranca.format("MM/yyyy")
LOG_CALCULO["mes_final"] = MINIMO(dataFinalCobranca, dataFinalCobrancaSelic).format("MM/yyyy")
LOG_CALCULO["indice_aplicado"] = indiceAcumulado
