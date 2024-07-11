package com.sd.demo.service

import android.app.Application
import com.sd.lib.service.FS

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FS.register(LoginServiceImpl::class.java)
    }
}