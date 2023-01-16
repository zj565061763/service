package com.sd.lib.service

private const val PackageRegister = "com.sd.lib.service.register"
private const val Separator = "_"

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class ModuleServiceInfo(
    val module: String,
    val service: String,
    val impl: String,
)

interface ServiceImplClassProvider {
    fun classes(): List<String>
}

internal fun registerFromCompiler(interfaceName: String) {
    val filename = "$PackageRegister.${interfaceName.replace(".", Separator)}"

    val registerClass = try {
        Class.forName(filename)
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
        return
    }

    val instance = registerClass.newInstance()
    if (instance is ServiceImplClassProvider) {
        instance.classes().forEach {
            FServiceManager.register(it)
        }
    }
}