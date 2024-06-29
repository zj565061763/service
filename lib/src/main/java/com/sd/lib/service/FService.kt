package com.sd.lib.service

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class FService(
    val name: String = "",
    val singleton: Boolean = false,
)

/**
 * 创建[T]的实现类对象
 */
inline fun <reified T> fs(name: String = ""): T {
    return FS.get(T::class.java, name)
}

/**
 * 创建[T]的实现类对象
 */
inline fun <reified T> fsRegister() {
    return FS.register(T::class.java)
}