package com.github.fragivity.example

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.github.fragivity.MoreNavOptions
import com.github.fragivity.applyHorizontalInOut
import com.github.fragivity.setFactory

class DemoApp : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()
        MoreNavOptions.setFactory {
            applyHorizontalInOut()
        }
    }
}