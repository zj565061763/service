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
class TestServiceImpl11 : TestService1

@FServiceImpl
class TestServiceImpl12 : TestService1