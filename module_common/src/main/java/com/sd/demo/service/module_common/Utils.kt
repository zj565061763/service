package com.sd.demo.service.module_common

import android.util.Log

inline fun logMsg(block: () -> String) {
    Log.i("service-demo", block())
}