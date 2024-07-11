package com.sd.demo.service

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.service.databinding.ActivityMainBinding
import com.sd.lib.service.fsGet

class MainActivity : AppCompatActivity() {
    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
        _binding.btn.setOnClickListener {
            fsGet<LoginService>().login()
        }
    }
}

inline fun logMsg(block: () -> Any?) {
    Log.i("service-demo", block().toString())
}