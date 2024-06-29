package com.sd.lib.service

import android.os.Build
import java.lang.reflect.Modifier

object FServiceManager {
    private val _implHolder: MutableMap<Class<*>, MutableMap<String, ServiceImplConfig>> = mutableMapOf()

    /**
     * 获取[serviceInterface]接口名称为[name]的实现类对象
     */
    @JvmStatic
    @JvmOverloads
    fun <T> get(
        serviceInterface: Class<T>,
        name: String = "",
    ): T {
        require(serviceInterface.isInterface) { "Require interface class" }
        synchronized(FServiceManager) {
            var holder = _implHolder[serviceInterface]
            if (holder == null) {
                registerFromCompiler(serviceInterface)
                holder = _implHolder[serviceInterface] ?: error("Implementation of ${serviceInterface.name} was not found")
            }

            val config = holder[name] ?: error("Implementation of ${serviceInterface.name} with name($name) was not found")
            @Suppress("UNCHECKED_CAST")
            return config.instance() as T
        }
    }

    /**
     * 注册实现类
     */
    @JvmStatic
    fun register(serviceImpl: Class<*>) {
        serviceImpl.run {
            require(!Modifier.isInterface(modifiers)) { "serviceImpl should not be an interface" }
            require(!Modifier.isAbstract(modifiers)) { "serviceImpl should not be abstract" }
        }

        val implAnnotation = serviceImpl.requireAnnotation(FServiceImpl::class.java)
        val serviceInterface = findServiceInterface(serviceImpl)

        synchronized(this@FServiceManager) {
            val holder = _implHolder.getOrPut(serviceInterface) { mutableMapOf() }

            val config = holder[implAnnotation.name]
            if (config != null) {
                if (config.serviceImpl == serviceImpl) return
                error("Can not register ${serviceImpl.name} because the implementation of ${serviceInterface.name} with name(${implAnnotation.name}) has been mapped to ${config.serviceImpl.name}")
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
        return serviceImpl.newInstance()
    }
}

private fun findServiceInterface(source: Class<*>): Class<*> {
    var ret: Class<*>? = null

    var current: Class<*> = source
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