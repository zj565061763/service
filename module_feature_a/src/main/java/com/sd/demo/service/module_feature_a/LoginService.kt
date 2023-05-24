package com.sd.demo.service.module_feature_a

import com.sd.demo.service.sample_common.LoginService
import com.sd.demo.service.sample_common.logMsg
import com.sd.lib.service.FServiceImpl

@FServiceImpl("ModuleFeatureA1")
class LoginServiceModuleFeatureA1 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}

@FServiceImpl("ModuleFeatureA2")
class LoginServiceModuleFeatureA2 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}