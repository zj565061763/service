package com.sd.demo.service

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.service.databinding.ActivityMainBinding
import com.sd.demo.service.sample_common.LoginService
import com.sd.lib.service.fs

class MainActivity : AppCompatActivity() {
    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)

        _binding.btn.setOnClickListener {
            fs<LoginService>().login()
            fs<LoginService>("F1").login()
            fs<LoginService>("F2").login()
        }
    }
}