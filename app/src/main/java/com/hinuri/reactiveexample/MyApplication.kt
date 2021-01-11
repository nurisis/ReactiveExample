package com.hinuri.reactiveexample

import android.app.Application
import org.koin.core.context.startKoin

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            modules(koinModule)
        }
    }
}