package com.sd.lib.service.compiler.mapping

/**
 * Annotation
 */
internal object FService : LibClass("FService")

/**
 * Annotation
 */
internal object FServiceImpl : LibClass("FServiceImpl")

/**
 * Annotation
 */
internal object ModuleServiceInfo : LibClass("ModuleServiceInfo") {
    val module = LibProperty("module")
    val service = LibProperty("service")
    val impl = LibProperty("impl")
}

/**
 * Interface
 */
internal object ServiceImplClassProvider : LibClass("ServiceImplClassProvider")