package com.sd.demo.service

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sd.demo.service.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val _binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
        _binding.btn.setOnClickListener {
            
        }
    }
}