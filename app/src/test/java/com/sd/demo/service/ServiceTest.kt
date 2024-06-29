package com.sd.demo.service

import com.sd.demo.service.utils.TestService
import com.sd.demo.service.utils.TestService0
import com.sd.demo.service.utils.TestService999
import com.sd.demo.service.utils.TestServiceImpl
import com.sd.demo.service.utils.TestServiceImpl01
import com.sd.demo.service.utils.TestServiceImpl02
import com.sd.demo.service.utils.TestServiceImpl999
import com.sd.demo.service.utils.TestServiceImplAbstract
import com.sd.demo.service.utils.TestServiceImplInterface
import com.sd.demo.service.utils.TestServiceImplName
import com.sd.demo.service.utils.TestServiceImplNoAnnotation
import com.sd.demo.service.utils.TestServiceImplNoInterface
import com.sd.demo.service.utils.TestServiceImplSingleton
import com.sd.lib.service.FService
import com.sd.lib.service.fs
import com.sd.lib.service.fsRegister
import org.junit.Assert.assertEquals
import org.junit.Test

class ServiceTest {
    @Test
    fun register() {
        // no annotation
        runCatching {
            fsRegister<TestServiceImplNoAnnotation>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals(
                "Annotation ${FService::class.java.simpleName} was not found in ${TestServiceImplNoAnnotation::class.java.name}",
                exception.message
            )
        }

        // is interface
        runCatching {
            fsRegister<TestServiceImplInterface>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals("${TestServiceImplInterface::class.java.name} is interface", exception.message)
        }

        // is abstract
        runCatching {
            fsRegister<TestServiceImplAbstract>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals("${TestServiceImplAbstract::class.java.name} is abstract", exception.message)
        }

        // no interface
        runCatching {
            fsRegister<TestServiceImplNoInterface>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals(
                "Interface was not found in ${TestServiceImplNoInterface::class.java.name}",
                exception.message
            )
        }
    }

    @Test
    fun registerMultiTimes() {
        fsRegister<TestServiceImpl01>()

        runCatching {
            fsRegister<TestServiceImpl01>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalStateException
            assertEquals(
                "Factory of ${TestService0::class.java.name} with name () already exist",
                exception.message
            )
        }

        runCatching {
            fsRegister<TestServiceImpl02>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalStateException
            assertEquals(
                "Factory of ${TestService0::class.java.name} with name () already exist",
                exception.message
            )
        }
    }

    @Test
    fun getNoRegister() {
        runCatching {
            fs<TestService999>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalStateException
            assertEquals(
                "Service (${TestService999::class.java.name}) was not found",
                exception.message
            )
        }

        fsRegister<TestServiceImpl999>()
        fs<TestService999>()

        runCatching {
            fs<TestService999>("999")
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalStateException
            assertEquals(
                "Service (${TestService999::class.java.name}) with name (999) was not found",
                exception.message
            )
        }
    }

    @Test
    fun get() {
        fsRegister<TestServiceImpl>()
        fsRegister<TestServiceImplName>()
        fsRegister<TestServiceImplSingleton>()

        assertEquals(true, fs<TestService>() is TestServiceImpl)
        assertEquals(true, fs<TestService>() !== fs<TestService>())

        assertEquals(true, fs<TestService>("name") is TestServiceImplName)
        assertEquals(true, fs<TestService>("name") !== fs<TestService>("name"))

        assertEquals(true, fs<TestService>("singleton") is TestServiceImplSingleton)
        assertEquals(true, fs<TestService>("singleton") === fs<TestService>("singleton"))

        assertEquals(true, fs<TestService>() !== fs<TestService>("name"))
        assertEquals(true, fs<TestService>() !== fs<TestService>("singleton"))
    }
}