package com.sd.demo.service

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.service.app_module_common.LoginService
import com.sd.lib.service.fService

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn -> {
                fService<LoginService>().login()
                fService<LoginService>("ModuleA1").login()
                fService<LoginService>("ModuleA2").login()
                fService<LoginService>("ModuleB1").login()
                fService<LoginService>("ModuleB2").login()
            }
        }
    }
}