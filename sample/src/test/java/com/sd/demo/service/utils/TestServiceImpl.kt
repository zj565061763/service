package com.sd.demo.service.utils

import com.sd.lib.service.FServiceImpl

@FServiceImpl
interface TestServiceImplInterface : TestService1

@FServiceImpl
abstract class TestServiceImplAbstract : TestService1

@FServiceImpl
class TestServiceImplMulti : TestService1, TestService2

@FServiceImpl
class TestServiceImpl11 : TestService1

@FServiceImpl
class TestServiceImpl12 : TestService1