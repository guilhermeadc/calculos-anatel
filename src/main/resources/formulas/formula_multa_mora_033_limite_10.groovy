/**
 * Multa de mora: Os débitos para com a União, oriundos de tributos e contribuições, cujos fatos geradores
 * ocorrerem a partir de 01/01/97, não pagos nos prazos previstos na legislação específica, serão acrescidos
 * de multa de mora calculada à taxa de 0,33% (trinta e três centésimos por cento), por dia de atraso até o
 * limite de 20% (vinte por cento). A multa de mora é calculada a partir do 1º (primeiro) dia subseqüente ao
 * do vencimento do prazo previsto para o pagamento do tributo, até o dia em que ocorrer o pagamento;
 */

VALIDACAO(lancamento.dataVencimento != null, 'Data de vencimento não pode ser nulo.')
def diasDeAtraso = MAXIMO(DATA_REFERENCIA - lancamento.dataVencimento, 0)
def taxa = MINIMO(diasDeAtraso * 0.0033, 0.20)
lancamento.multaMora = lancamento.valorOriginal * taxa