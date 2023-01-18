package com.sd.demo.service

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.service.module_common.LoginService
import com.sd.lib.service.fs

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn -> {
                fs<LoginService>().login()
                fs<LoginService>("ModuleFeature1").login()
                fs<LoginService>("ModuleFeature2").login()
            }
        }
    }
}