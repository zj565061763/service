package com.sd.demo.service.app_module_b

import com.sd.demo.service.app_module_common.LoginService
import com.sd.demo.service.app_module_common.logMsg
import com.sd.lib.service.FServiceImpl

@FServiceImpl("ModuleB1")
class LoginServiceModuleB1 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}

@FServiceImpl("ModuleB2")
class LoginServiceModuleB2 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}