package com.sd.lib.service.compiler.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSNode
import com.sd.lib.service.compiler.LibVersion
import com.sd.lib.service.compiler.OptionsKeyModuleName
import com.sd.lib.service.compiler.OptionsValueModuleMain

internal abstract class BaseProcessor(
    val env: SymbolProcessorEnvironment,
    private val main: Boolean = false,
) : SymbolProcessor {

    val moduleName: String
        get() {
            val moduleName = env.options[OptionsKeyModuleName]
            if (moduleName.isNullOrEmpty()) error("$OptionsKeyModuleName was not found in ksp options")
            return moduleName
        }

    private val isMainModule: Boolean
        get() = moduleName == OptionsValueModuleMain

    final override fun process(resolver: Resolver): List<KSAnnotated> {
        if (main && !isMainModule) return listOf()
        return processImpl(resolver)
    }

    final override fun onError() {
        super.onError()
        if (main && !isMainModule) return
        errorImpl()
    }

    final override fun finish() {
        super.finish()
        if (main && !isMainModule) return
        finishImpl()
    }

    abstract fun processImpl(resolver: Resolver): List<KSAnnotated>

    protected open fun errorImpl() {
        log("---------- $moduleName error ----------")
    }

    protected open fun finishImpl() {
        log("---------- $moduleName finish ----------")
    }

    fun log(message: String, symbol: KSNode? = null) {
        env.logger.warn("$LibVersion ${javaClass.simpleName} $message", symbol)
    }
}