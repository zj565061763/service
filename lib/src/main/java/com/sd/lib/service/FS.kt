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
     * 注册
     */
    @JvmStatic
    fun register(service: Class<*>) {
        synchronized(FS) {
            _scope.register(service)
        }
    }
}
