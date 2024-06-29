package com.sd.demo.service

import com.sd.demo.service.utils.TestServiceImplAbstract
import com.sd.demo.service.utils.TestServiceImplInterface
import com.sd.lib.service.fsRegister
import org.junit.Assert.assertEquals
import org.junit.Test

class ServiceTest {
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
}