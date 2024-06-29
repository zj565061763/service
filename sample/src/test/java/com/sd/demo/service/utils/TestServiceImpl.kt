package com.sd.demo.service.utils

import com.sd.lib.service.FServiceImpl

class TestServiceImplNoAnnotation

@FServiceImpl
interface TestServiceImplInterface
@FServiceImpl
abstract class TestServiceImplAbstract
@FServiceImpl
class TestServiceImplNoService
@FServiceImpl
class TestServiceImplMultiService : TestService1, TestService2

@FServiceImpl
class TestServiceImpl01 : TestService0
@FServiceImpl
class TestServiceImpl02 : TestService0

@FServiceImpl
class TestServiceImpl : TestService
@FServiceImpl("name")
class TestServiceImplName : TestService