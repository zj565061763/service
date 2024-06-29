package com.sd.lib.service

fun interface FServiceFactory<T> {
    fun create(): T
}