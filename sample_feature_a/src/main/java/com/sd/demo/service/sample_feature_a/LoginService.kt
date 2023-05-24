package com.sd.demo.service.sample_feature_a

import com.sd.demo.service.sample_common.LoginService
import com.sd.demo.service.sample_common.logMsg
import com.sd.lib.service.FServiceImpl

@FServiceImpl("FeatureA1")
class LoginServiceFeatureA1 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}

@FServiceImpl("FeatureA2")
class LoginServiceFeatureA2 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}