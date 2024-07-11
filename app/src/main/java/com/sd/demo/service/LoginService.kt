package com.sd.demo.service

import com.sd.lib.service.FService

interface LoginService {
    fun login()
}

@FService
class LoginServiceImpl : LoginService {
    override fun login() {
        logMsg { "$this login" }
    }
}