package com.sd.demo.service.module_feature

import com.sd.demo.service.app_module_common.LoginService
import com.sd.demo.service.app_module_common.logMsg
import com.sd.lib.service.FServiceImpl

@FServiceImpl("ModuleA1")
class LoginServiceModuleA1 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}

@FServiceImpl("ModuleA2")
class LoginServiceModuleA2 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}