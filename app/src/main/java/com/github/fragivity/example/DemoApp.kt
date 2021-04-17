package com.github.fragivity.example

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

class DemoApp : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }
}