package com.sd.demo.service

import com.sd.demo.service.utils.TestService1
import com.sd.demo.service.utils.TestService2
import com.sd.demo.service.utils.TestServiceImplAbstract
import com.sd.demo.service.utils.TestServiceImplInterface
import com.sd.demo.service.utils.TestServiceImplMultiService
import com.sd.demo.service.utils.TestServiceImplNoAnnotation
import com.sd.demo.service.utils.TestServiceImplNoService
import com.sd.lib.service.FService
import com.sd.lib.service.FServiceImpl
import com.sd.lib.service.fsRegister
import org.junit.Assert.assertEquals
import org.junit.Test

class ServiceTest {
    @Test
    fun registerNoAnnotation() {
        runCatching {
            fsRegister<TestServiceImplNoAnnotation>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals(
                "Annotation ${FServiceImpl::class.java.simpleName} was not found in ${TestServiceImplNoAnnotation::class.java.name}",
                exception.message
            )
        }

        runCatching {
            fsRegister<TestServiceImplAbstract>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals("${TestServiceImplAbstract::class.java.name} is abstract", exception.message)
        }
    }

    @Test
    fun registerNotClass() {
        runCatching {
            fsRegister<TestServiceImplInterface>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals("${TestServiceImplInterface::class.java.name} is interface", exception.message)
        }

        runCatching {
            fsRegister<TestServiceImplAbstract>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals("${TestServiceImplAbstract::class.java.name} is abstract", exception.message)
        }
    }

    @Test
    fun registerNoService() {
        runCatching {
            fsRegister<TestServiceImplNoService>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals(
                "Interface marked with annotation @${FService::class.java.simpleName} was not found in ${TestServiceImplNoService::class.java.name} super types",
                exception.message
            )
        }
    }

    @Test
    fun registerMultiService() {
        runCatching {
            fsRegister<TestServiceImplMultiService>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals(
                "More than one service interface present in ${TestServiceImplMultiService::class.java.name} (${TestService1::class.java.name}) (${TestService2::class.java.name})",
                exception.message
            )
        }
    }
}