package com.sd.lib.service

import java.lang.reflect.Modifier

internal class ServiceScope {
    private val _holder: MutableMap<Class<*>, MutableMap<String, ServiceInfo>> = mutableMapOf()

    /**
     * 获取名称为[name]的[service]对象
     */
    fun <T> get(
        service: Class<T>,
        name: String,
    ): T {
        val serviceHolder = _holder[service] ?: error("Service (${service.name}) not found")
        val serviceInfo = serviceHolder[name] ?: error("Service (${service.name}) with name ($name) not found")
        @Suppress("UNCHECKED_CAST")
        return serviceInfo.getService() as T
    }

    /**
     * 注册[service]，并把[service]和他所有实现的接口绑定，
     * 当外部获取接口实例时，会调用[service]的无参构造方法创建对象返回
     */
    fun register(service: Class<*>) {
        val annotation = service.requireAnnotation(FService::class.java)
        findInterfaces(service).forEach {
            @Suppress("UNCHECKED_CAST")
            factory(
                service = it as Class<Any>,
                name = annotation.name,
                singleton = annotation.singleton,
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
        factory: () -> T,
    ) {
        val serviceHolder = _holder.getOrPut(service) { mutableMapOf() }

        val serviceInfo = serviceHolder[name]
        if (serviceInfo != null) {
            error("Factory of ${service.name} with name (${name}) already exist")
        }

        serviceHolder[name] = ServiceInfo(
            singleton = singleton,
            factory = factory,
        )
    }
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

private fun findInterfaces(source: Class<*>): Collection<Class<*>> {
    source.run {
        require(!Modifier.isInterface(modifiers)) { "${source.name} is interface" }
        require(!Modifier.isAbstract(modifiers)) { "${source.name} is abstract" }
    }

    val collection: MutableSet<Class<*>> = mutableSetOf()

    var current: Class<*> = source
    while (true) {
        val interfaces = current.interfaces
        if (interfaces.isNullOrEmpty()) break
        collection.addAll(interfaces)
        current = current.superclass ?: break
    }

    return collection.also {
        require(it.isNotEmpty()) {
            "No interface was found in ${source.name}"
        }
    }
}

private fun <T : Annotation> Class<*>.requireAnnotation(annotation: Class<T>): T {
    return requireNotNull(getAnnotation(annotation)) {
        "Annotation ${annotation.simpleName} was not found in ${this.name}"
    }
}