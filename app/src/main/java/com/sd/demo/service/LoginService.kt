package com.sd.demo.service

import com.sd.demo.service.app_module_common.LoginService
import com.sd.demo.service.app_module_common.logMsg
import com.sd.lib.service.FServiceImpl

@FServiceImpl(singleton = true)
class LoginServiceApp : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}