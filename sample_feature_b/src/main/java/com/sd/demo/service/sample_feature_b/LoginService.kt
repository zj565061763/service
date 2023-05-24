package com.sd.demo.service.sample_feature_b

import com.sd.demo.service.sample_common.LoginService
import com.sd.demo.service.sample_common.logMsg
import com.sd.lib.service.FServiceImpl

@FServiceImpl("FeatureB1")
class LoginServiceFeatureB1 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}

@FServiceImpl("FeatureB2")
class LoginServiceFeatureB2 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}