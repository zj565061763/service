package com.sd.demo.service.utils

import com.sd.lib.service.FService

class TestServiceImplNoAnnotation
@FService
interface TestServiceImplInterface
@FService
abstract class TestServiceImplAbstract
@FService
class TestServiceImplNoInterface

@FService
class TestServiceImpl01 : TestService0
@FService
class TestServiceImpl02 : TestService0

@FService
class TestServiceImpl999 : TestService999

@FService
class TestServiceImplMultiService : TestService1, TestService2

@FService
class TestServiceImpl : TestService
@FService("name")
class TestServiceImplName : TestService
@FService("singleton", true)
class TestServiceImplSingleton : TestService