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

internal fun registerFromCompiler(serviceInterface: Class<*>) {
    val filename = "$PackageRegister.${serviceInterface.name.replace(".", Separator)}"

    val instance = try {
        Class.forName(filename).getDeclaredConstructor().newInstance()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } ?: return

    if (instance is ServiceImplClassProvider) {
        for (item in instance.classes()) {
            val serviceImpl = try {
                Class.forName(item)
            } catch (e: Exception) {
                e.printStackTrace()
                continue
            }
            FServiceManager.register(serviceImpl)
        }
    }
}