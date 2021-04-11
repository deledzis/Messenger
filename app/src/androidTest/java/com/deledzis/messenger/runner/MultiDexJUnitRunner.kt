package com.deledzis.messenger.runner

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import androidx.multidex.MultiDex
import androidx.test.runner.AndroidJUnitRunner
import com.deledzis.messenger.App

class MultiDexJUnitRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle?) {
        MultiDex.install(targetContext)
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        super.onCreate(arguments)
    }

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, App::class.java.name, context)
    }

}