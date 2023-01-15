package com.sd.lib.service

@Target(AnnotationTarget.CLASS)
annotation class FService

@Target(AnnotationTarget.CLASS)
annotation class FServiceImpl(
    val name: String = "",
    val singleton: Boolean = false,
)

/**
 * 创建[T]的实现类对象
 */
inline fun <reified T> fService(name: String = ""): T {
    return FServiceManager.get(T::class.java, name)
}