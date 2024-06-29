package com.sd.lib.service

import android.os.Build
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
        val serviceHolder = _holder[service] ?: error("Service (${service.name}) was not found")
        val serviceInfo = serviceHolder[name] ?: error("Service (${service.name}) with name ($name) was not found")
        @Suppress("UNCHECKED_CAST")
        return serviceInfo.getService() as T
    }

    /**
     * 注册
     */
    fun register(service: Class<*>) {
        val annotation = service.requireAnnotation(FService::class.java)
        findInterfaces(service).forEach {
            @Suppress("UNCHECKED_CAST")
            val clazz = it as Class<Any>
            setFactory(
                clazz = clazz,
                name = annotation.name,
                singleton = annotation.singleton,
                factory = { service.getDeclaredConstructor().newInstance() }
            )
        }
    }

    /**
     * 设置[clazz]对应的工厂[factory]
     */
    fun <T : Any> setFactory(
        clazz: Class<T>,
        name: String,
        singleton: Boolean,
        factory: () -> T,
    ) {
        val serviceHolder = _holder.getOrPut(clazz) { mutableMapOf() }

        val serviceInfo = serviceHolder[name]
        if (serviceInfo != null) {
            error("Factory of ${clazz.name} with name (${name}) already exist")
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
            "Interface was not found in ${source.name}"
        }
    }
}

private fun <T : Annotation> Class<*>.requireAnnotation(clazz: Class<T>): T {
    return requireNotNull(getAnnotationCompat(clazz)) {
        "Annotation ${clazz.simpleName} was not found in ${this.name}"
    }
}

private fun <T : Annotation> Class<*>.getAnnotationCompat(clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        getDeclaredAnnotation(clazz)
    } else {
        getAnnotation(clazz)
    }
}