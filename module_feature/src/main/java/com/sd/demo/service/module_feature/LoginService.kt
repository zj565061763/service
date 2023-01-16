package com.sd.demo.service.module_feature

import com.sd.demo.service.module_common.LoginService
import com.sd.demo.service.module_common.logMsg
import com.sd.lib.service.FServiceImpl

@FServiceImpl("ModuleFeature1")
class LoginServiceModuleFeature1 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}

@FServiceImpl("ModuleFeature2")
class LoginServiceModuleFeature2 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}