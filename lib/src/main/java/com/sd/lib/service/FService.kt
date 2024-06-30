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
 * 注册[T]，并把[T]和他所有实现的接口绑定，
 * 当外部获取接口实例时，会调用[T]的无参构造方法创建对象返回
 */
inline fun <reified T> fsRegister() {
    FS.register(T::class.java)
}

/**
 * 设置[T]对应的工厂[factory]
 * @param name 实例的名称
 * @param singleton 是否单例
 */
inline fun <reified T : Any> fsSetFactory(
    name: String = "",
    singleton: Boolean = false,
    noinline factory: () -> T,
) {
    FS.setFactory(
        clazz = T::class.java,
        name = name,
        singleton = singleton,
        factory = factory,
    )
}