package com.sd.demo.service.utils

import com.sd.lib.service.FService

@FService
interface TestService {
    fun method1(): String = ""
}

@FService
interface TestService0 {
    fun method1(): String = "0"
}

@FService
interface TestService1 {
    fun method1(): String = "1"
}

@FService
interface TestService2 {
    fun method2(): String = "2"
}

@FService
interface TestService999 {
    fun method2(): String = "999"
}