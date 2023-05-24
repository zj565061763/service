package com.sd.demo.service.sample_common

import com.sd.lib.service.FServiceImpl

@FServiceImpl("F1")
class LoginServiceF1 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}

@FServiceImpl("F2")
class LoginServiceF2 : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}