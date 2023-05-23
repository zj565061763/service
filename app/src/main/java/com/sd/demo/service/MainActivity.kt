package com.sd.demo.service

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sd.demo.service.module_common.LoginService
import com.sd.demo.service.ui.theme.AppTheme
import com.sd.lib.service.fs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Content(
                    onClick = {
                        fs<LoginService>().login()
                        fs<LoginService>("ModuleFeatureA1").login()
                        fs<LoginService>("ModuleFeatureA2").login()
                        fs<LoginService>("ModuleFeatureB1").login()
                        fs<LoginService>("ModuleFeatureB2").login()
                    },
                )
            }
        }
    }
}

@Composable
private fun Content(
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Button(
            onClick = onClick
        ) {
            Text(text = "button")
        }
    }
}