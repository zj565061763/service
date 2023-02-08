package com.sd.lib.service

import java.lang.reflect.Modifier

object FServiceManager {
    private val _mapInterfaceImpl: MutableMap<String, MutableMap<String, ServiceImplConfig>> = hashMapOf()

    /**
     * 获取[clazz]接口的实现类对象
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(
        clazz: Class<T>,
        name: String = "",
    ): T {
        require(clazz.isInterface) { "Require class is interface" }
        val serviceInterface = clazz.name
        synchronized(this@FServiceManager) {
            var holder = _mapInterfaceImpl[serviceInterface]
            if (holder == null) {
                registerFromCompiler(serviceInterface)
                holder = _mapInterfaceImpl[serviceInterface]
                checkNotNull(holder) { "Implementation of $serviceInterface was not found" }
            }

            val config = holder[name] ?: error("Implementation of $serviceInterface with name($name) was not found")
            return config.instance() as T
        }
    }

    /**
     * 注册实现类
     */
    fun register(implClass: Class<*>) {
        register(implClass.name)
    }

    /**
     * 注册实现类
     */
    internal fun register(
        implClassName: String,
        serviceClassName: String = ""
    ) {
        val implClass = Class.forName(implClassName).also {
            it.requireIsClass()
        }
        val annotation = implClass.requireAnnotation(FServiceImpl::class.java)

        val serviceInterface = if (serviceClassName.isEmpty()) {
            findServiceInterface(implClass).name
        } else {
            Class.forName(serviceClassName)?.let {
                require(it.isInterface) { "serviceClassName should be an interface" }
                require(it.isAssignableFrom(implClass)) { "$serviceClassName is not assignable from $implClassName" }
                it.requireAnnotation(FService::class.java)
            }
            serviceClassName
        }

        synchronized(this@FServiceManager) {
            val holder = _mapInterfaceImpl[serviceInterface] ?: hashMapOf<String, ServiceImplConfig>().also {
                _mapInterfaceImpl[serviceInterface] = it
            }

            val config = holder[annotation.name]
            if (config != null) {
                if (config.implClass == implClass) return
                error("Implementation of $serviceInterface with name(${annotation.name}) has been mapped to ${config.implClass.name}")
            }

            holder[annotation.name] = ServiceImplConfig(
                implClass = implClass,
                singleton = annotation.singleton,
            )
        }
    }
}

private class ServiceImplConfig(
    val implClass: Class<*>,
    val singleton: Boolean,
) {
    private var _instance: Any? = null

    fun instance(): Any {
        return if (singleton) {
            _instance ?: newImplInstance().also {
                _instance = it
            }
        } else {
            newImplInstance()
        }
    }

    private fun newImplInstance(): Any {
        return implClass.newInstance()
    }
}

private fun findServiceInterface(source: Class<*>): Class<*> {
    var ret: Class<*>? = null

    var current = source
    while (true) {
        val interfaces = current.interfaces
        if (interfaces.isEmpty()) break

        for (item in interfaces) {
            if (item.isAnnotationPresent(FService::class.java)) {
                if (ret == null) {
                    ret = item
                } else {
                    error("More than one service interface present in ${source.name} : ${ret.name}, ${item.name}")
                }
            }
        }

        current = current.superclass ?: break
    }

    return checkNotNull(ret) {
        "Interface marked with annotation @${FService::class.java.simpleName} was not found in ${source.name} super types"
    }
}

private fun Class<*>.requireIsClass() {
    require(!Modifier.isInterface(modifiers)) { "Class should not be an interface" }
    require(!Modifier.isAbstract(modifiers)) { "Class should not be abstract" }
}

private fun <T : Annotation> Class<*>.requireAnnotation(clazz: Class<T>): T {
    return requireNotNull(getDeclaredAnnotation(clazz)) {
        "Annotation ${clazz.simpleName} was not found in ${this.name}"
    }
}