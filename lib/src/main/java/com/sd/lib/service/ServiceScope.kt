package com.sd.lib.service

import java.lang.reflect.Modifier

internal class ServiceScope {
    private val _holder: MutableMap<Class<*>, MutableMap<String, ServiceInfo>> = mutableMapOf()

    /** 缺席回调 */
    private var _absentCallback: FServiceAbsentCallback? = null

    /**
     * 设置缺席回调
     */
    fun setAbsentCallback(callback: FServiceAbsentCallback?) {
        _absentCallback = callback
    }

    /**
     * 获取名称为[name]的[service]对象，如果不存在则抛异常
     */
    fun <T> get(
        service: Class<T>,
        name: String,
    ): T {
        return getOrNull(service, name)
            ?: error("Service (${service.name}) with name ($name) not found")
    }

    /**
     * 获取名称为[name]的[service]对象，如果不存在则返回null
     */
    fun <T> getOrNull(
        service: Class<T>,
        name: String,
    ): T? {
        var serviceInfo = _holder[service]?.get(name)

        _absentCallback?.let { callback ->
            if (serviceInfo == null) {
                callback.onAbsent(service, name)
                serviceInfo = _holder[service]?.get(name)
            }
        }

        val info = serviceInfo ?: return null
        @Suppress("UNCHECKED_CAST")
        return info.getService() as T
    }

    /**
     * 注册[service]，并把[service]和他所有实现的接口绑定，
     * 当外部获取接口实例时，会调用[service]的无参构造方法创建对象返回
     */
    fun register(
        service: Class<*>,
        factoryMode: FactoryMode,
    ) {
        val annotation = requireNotNull(service.getAnnotation(FService::class.java)) {
            "Annotation ${FService::class.java.simpleName} was not found in ${service.name}"
        }
        findInterfaces(service).forEach {
            @Suppress("UNCHECKED_CAST")
            factory(
                service = it as Class<Any>,
                name = annotation.name,
                singleton = annotation.singleton,
                factoryMode = factoryMode,
                factory = { service.getDeclaredConstructor().newInstance() }
            )
        }
    }

    /**
     * 设置[service]对应的工厂[factory]
     * @param name 实例的名称
     * @param singleton 是否单例
     */
    fun <T : Any> factory(
        service: Class<T>,
        name: String,
        singleton: Boolean,
        factoryMode: FactoryMode,
        factory: () -> T,
    ) {
        val serviceHolder = _holder.getOrPut(service) { mutableMapOf() }

        when (factoryMode) {
            FactoryMode.CancelIfExist -> {
                if (serviceHolder.containsKey(name)) {
                    return
                }
            }
            FactoryMode.ThrowIfExist -> {
                if (serviceHolder.containsKey(name)) {
                    error("Factory of ${service.name} with name (${name}) already exist")
                }
            }
        }

        serviceHolder[name] = ServiceInfo(
            singleton = singleton,
            factory = factory,
        )
    }
}

enum class FactoryMode {
    /** 如果存在则取消 */
    CancelIfExist,

    /** 如果存在则抛异常 */
    ThrowIfExist,
}

private class ServiceInfo(
    val singleton: Boolean,
    val factory: () -> Any,
) {
    private var _service: Any? = null

    fun getService(): Any {
        return if (singleton) {
            _service ?: factory().also { _service = it }
        } else {
            factory()
        }
    }
}

private fun findInterfaces(impl: Class<*>): Array<Class<*>> {
    impl.run {
        require(!Modifier.isInterface(modifiers)) { "${impl.name} is interface" }
        require(!Modifier.isAbstract(modifiers)) { "${impl.name} is abstract" }
    }
    return impl.interfaces.also {
        require(it.isNotEmpty()) {
            "No interface was found in ${impl.name}"
        }
    }
}