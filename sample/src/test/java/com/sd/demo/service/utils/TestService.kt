package com.sd.demo.service.utils

import com.sd.lib.service.FService

@FService
interface TestService1 {
    fun method1(): String = "1"
}

@FService
interface TestService2 {
    fun method2(): String = "2"
}