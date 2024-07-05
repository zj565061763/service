package com.sd.lib.service

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class FService(
    /** 名称 */
    val name: String = "",

    /** 是否单例 */
    val singleton: Boolean = false,
)