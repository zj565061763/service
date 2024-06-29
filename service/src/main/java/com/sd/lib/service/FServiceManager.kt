package com.sd.lib.service

import android.os.Build
import java.lang.reflect.Modifier

object FServiceManager {
    private val _implHolder: MutableMap<Class<*>, MutableMap<String, ServiceImplConfig>> = mutableMapOf()

    /**
     * 获取[service]接口名称为[name]的实现类对象
     */
    @JvmStatic
    @JvmOverloads
    fun <T> get(
        service: Class<T>,
        name: String = "",
    ): T {
        require(service.isInterface) { "Require interface class" }
        synchronized(FServiceManager) {
            var holder = _implHolder[service]
            if (holder == null) {
                registerFromCompiler(service)
                holder = _implHolder[service] ?: error("Implementation of ${service.name} was not found")
            }

            val config = holder[name] ?: error("Implementation of ${service.name} with name($name) was not found")
            @Suppress("UNCHECKED_CAST")
            return config.instance() as T
        }
    }

    /**
     * 注册实现类
     */
    @JvmStatic
    fun registerImpl(serviceImpl: Class<*>) {
        val implAnnotation = serviceImpl.requireAnnotation(FServiceImpl::class.java)
        val serviceInterface = findServiceInterface(serviceImpl)
        synchronized(FServiceManager) {
            val holder = _implHolder.getOrPut(serviceInterface) { mutableMapOf() }

            val config = holder[implAnnotation.name]
            if (config != null) {
                if (config.serviceImpl == serviceImpl) return
                throw IllegalArgumentException("Implementation of ${serviceInterface.name} with name(${implAnnotation.name}) has been mapped to ${config.serviceImpl.name}")
            }

            holder[implAnnotation.name] = ServiceImplConfig(
                serviceImpl = serviceImpl,
                singleton = implAnnotation.singleton,
            )
        }
    }
}

private class ServiceImplConfig(
    val serviceImpl: Class<*>,
    val singleton: Boolean,
) {
    private var _instance: Any? = null

    fun instance(): Any {
        return if (singleton) {
            _instance ?: newServiceImplInstance().also {
                _instance = it
            }
        } else {
            newServiceImplInstance()
        }
    }

    private fun newServiceImplInstance(): Any {
        return serviceImpl.getDeclaredConstructor().newInstance()
    }
}

private fun findServiceInterface(source: Class<*>): Class<*> {
    source.run {
        require(!Modifier.isInterface(modifiers)) { "${source.name} is interface" }
        require(!Modifier.isAbstract(modifiers)) { "${source.name} is abstract" }
    }

    var ret: Class<*>? = null

    var current: Class<*> = source
    while (true) {
        val interfaces = current.interfaces
        if (interfaces.isNullOrEmpty()) break
        for (item in interfaces) {
            if (item.isAnnotationPresent(FService::class.java)) {
                if (ret == null) {
                    ret = item
                } else {
                    throw IllegalArgumentException("More than one service interface present in ${source.name} (${ret.name}) (${item.name})")
                }
            }
        }
        current = current.superclass ?: break
    }

    return requireNotNull(ret) {
        "Interface marked with annotation @${FService::class.java.simpleName} was not found in ${source.name} super types"
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