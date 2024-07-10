package com.sd.demo.service

import com.sd.demo.service.utils.TestService
import com.sd.demo.service.utils.TestService0
import com.sd.demo.service.utils.TestService1
import com.sd.demo.service.utils.TestService2
import com.sd.demo.service.utils.TestService999
import com.sd.demo.service.utils.TestServiceImpl
import com.sd.demo.service.utils.TestServiceImpl01
import com.sd.demo.service.utils.TestServiceImpl02
import com.sd.demo.service.utils.TestServiceImpl999
import com.sd.demo.service.utils.TestServiceImplAbstract
import com.sd.demo.service.utils.TestServiceImplInterface
import com.sd.demo.service.utils.TestServiceImplMultiService
import com.sd.demo.service.utils.TestServiceImplName
import com.sd.demo.service.utils.TestServiceImplNoAnnotation
import com.sd.demo.service.utils.TestServiceImplNoInterface
import com.sd.demo.service.utils.TestServiceImplNoneSingleton
import com.sd.lib.service.FS
import com.sd.lib.service.FService
import com.sd.lib.service.FactoryMode
import com.sd.lib.service.fsGet
import org.junit.Assert.assertEquals
import org.junit.Test

class ServiceTest {
    @Test
    fun testRegister() {
        // no annotation
        runCatching {
            FS.register(TestServiceImplNoAnnotation::class.java)
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals(
                "Annotation ${FService::class.java.simpleName} was not found in ${TestServiceImplNoAnnotation::class.java.name}",
                exception.message
            )
        }

        // is interface
        runCatching {
            FS.register(TestServiceImplInterface::class.java)
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals("${TestServiceImplInterface::class.java.name} is interface", exception.message)
        }

        // is abstract
        runCatching {
            FS.register(TestServiceImplAbstract::class.java)
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals("${TestServiceImplAbstract::class.java.name} is abstract", exception.message)
        }

        // no interface
        runCatching {
            FS.register(TestServiceImplNoInterface::class.java)
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalArgumentException
            assertEquals(
                "No interface was found in ${TestServiceImplNoInterface::class.java.name}",
                exception.message
            )
        }
    }

    @Test
    fun testRegisterMultiTimes() {
        FS.register(TestServiceImpl01::class.java)
        FS.register(TestServiceImpl01::class.java)

        runCatching {
            FS.register(TestServiceImpl01::class.java, factoryMode = FactoryMode.ThrowIfExist)
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalStateException
            assertEquals(
                "Factory of ${TestService0::class.java.name} with name () already exist",
                exception.message
            )
        }

        FS.register(TestServiceImpl02::class.java, factoryMode = FactoryMode.CancelIfExist)
        assertEquals(true, fsGet<TestService0>() is TestServiceImpl01)
    }

    @Test
    fun testGetNoRegister() {
        runCatching {
            fsGet<TestService999>()
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalStateException
            assertEquals(
                "Service (${TestService999::class.java.name}) not found",
                exception.message
            )
        }

        FS.register(TestServiceImpl999::class.java)
        fsGet<TestService999>()

        runCatching {
            fsGet<TestService999>("999")
        }.let { result ->
            val exception = result.exceptionOrNull() as IllegalStateException
            assertEquals(
                "Service (${TestService999::class.java.name}) with name (999) not found",
                exception.message
            )
        }
    }

    @Test
    fun testGet() {
        FS.register(TestServiceImpl::class.java)
        FS.register(TestServiceImplName::class.java)
        FS.register(TestServiceImplNoneSingleton::class.java)

        assertEquals(true, fsGet<TestService>() is TestServiceImpl)
        assertEquals(true, fsGet<TestService>() === fsGet<TestService>())

        assertEquals(true, fsGet<TestService>("name") is TestServiceImplName)
        assertEquals(true, fsGet<TestService>("name") === fsGet<TestService>("name"))

        assertEquals(true, fsGet<TestService>("NoneSingleton") is TestServiceImplNoneSingleton)
        assertEquals(true, fsGet<TestService>("NoneSingleton") !== fsGet<TestService>("NoneSingleton"))

        assertEquals(true, fsGet<TestService>() !== fsGet<TestService>("name"))
        assertEquals(true, fsGet<TestService>() !== fsGet<TestService>("NoneSingleton"))
    }

    @Test
    fun testFactory() {
        FS.factory(TestService1::class.java) { TestServiceImplMultiService() }
        FS.factory(TestService2::class.java) { TestServiceImplMultiService() }

        assertEquals(true, fsGet<TestService1>() is TestServiceImplMultiService)
        assertEquals(true, fsGet<TestService2>() is TestServiceImplMultiService)
    }
}