package com.sd.lib.service

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class FService(
    val name: String = "",
    val singleton: Boolean = false,
)

/**
 * 获取名称为[name]的[T]对象
 */
inline fun <reified T> fsGet(name: String = ""): T {
    return FS.get(T::class.java, name)
}