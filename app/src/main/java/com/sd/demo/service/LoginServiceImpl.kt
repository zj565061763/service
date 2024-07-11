package com.sd.demo.service

import com.sd.lib.service.FService

@FService
class LoginServiceImpl : LoginService {
    override fun login() {
        logMsg { "$this login" }
        test()
    }

    private fun test() {
        logMsg { "$this test" }
    }
}