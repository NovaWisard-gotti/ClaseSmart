package com.educalab.clasesmart

import android.app.Application
import com.educalab.clasesmart.di.AppContainer

class ClaseSmartApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
