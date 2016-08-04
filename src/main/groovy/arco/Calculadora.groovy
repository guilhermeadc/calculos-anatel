package arco

import org.codehaus.groovy.control.CompilerConfiguration

/**
 * Created by guilhermeadc on 02/08/16.
 */
class Calculadora {

    def script = null
    def scriptEngine = null
    def logUltimoCalculo = null
    def DATA_REFERENCIA = new Date()

    private final String[] localizacaoFormular = ["src/main/resources/formulas", "src/main/resources/formulas/multaDeMora",
        "src/main/resources/formulas/jurosDeMora", "src/main/resources/formulas/atualizacaoMonetaria", "src/main/resources/formulas/multaDeEdital"]

    public Calculadora() {
        def config = new CompilerConfiguration()
        config.scriptBaseClass = CustomScript.class.name
        scriptEngine = new GroovyScriptEngine(localizacaoFormular)
        scriptEngine.setConfig(config)
    }

    private void configurarVariaveis(Script script) {
        script.DATA_REFERENCIA = DATA_REFERENCIA;
    }

    private void configurarFuncoes(Script script) {
        script.metaClass.INDICE_ECONOMICO = {String nomeIndice, Date dataInicial, Date dataFinal ->
            this.INDICE_ECONOMICO(nomeIndice, dataInicial, dataFinal);
        }
    }

    public Lancamento executarFormula(String nomeScript, Lancamento lancamento = null, HashMap parametros = null) {
        def binding = new Binding()
        logUltimoCalculo = [:]
        lancamento = lancamento ?: new Lancamento()
        binding.setProperty("lancamento", lancamento)
        binding.setProperty("parametros", parametros ?: [:])
        binding.setProperty("LOG_CALCULO", logUltimoCalculo)
        def script = scriptEngine.createScript(nomeScript, binding)
        configurarVariaveis(script)
        configurarFuncoes(script)
        script.run()
        return lancamento
    }

    public Lancamento executarFormula(List listaScripts, Lancamento lancamento = null, HashMap parametros = null) {
        assert listaScripts != null, "Parâmetro [listaScripts] não pode ser nulo."
        listaScripts.each {
            this.executarFormula(it, lancamento, parametros)
        }
        return lancamento
    }

    //Funções disponíveis dentro do contexto de execução das fórmulas
    def INDICE_ECONOMICO(String nomeIndice, Date dataInicial, Date dataFinal){
        return 0
    }
}