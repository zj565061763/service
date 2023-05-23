package com.sd.demo.service.module_feature_b

import com.sd.demo.service.module_common.LoginService
import com.sd.demo.service.module_common.logMsg
import com.sd.lib.service.FServiceImpl

@FServiceImpl("ModuleFeatureB1")
class LoginServiceModuleFeatureB1 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}

@FServiceImpl("ModuleFeatureB2")
class LoginServiceModuleFeatureB2 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}