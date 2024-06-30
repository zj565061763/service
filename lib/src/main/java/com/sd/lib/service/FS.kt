package com.sd.lib.service

object FS {
    private val _scope = ServiceScope()

    /**
     * 获取名称为[name]的[service]对象
     */
    @JvmStatic
    @JvmOverloads
    fun <T> get(
        service: Class<T>,
        name: String = "",
    ): T {
        synchronized(FS) {
            return _scope.get(service, name)
        }
    }

    /**
     * 注册[service]，并把[service]和他所有实现的接口绑定，
     * 当外部获取接口实例时，会调用[service]的无参构造方法创建对象返回
     */
    @JvmStatic
    fun register(service: Class<*>) {
        synchronized(FS) {
            _scope.register(service)
        }
    }

    /**
     * 设置[service]对应的工厂[factory]
     * @param name 实例的名称
     * @param singleton 是否单例
     */
    @JvmOverloads
    @JvmStatic
    fun <T : Any> factory(
        service: Class<T>,
        name: String = "",
        singleton: Boolean = false,
        factory: () -> T,
    ) {
        synchronized(FS) {
            _scope.setFactory(
                service = service,
                name = name,
                singleton = singleton,
                factory = factory,
            )
        }
    }
}