package com.example.tasuku

import android.app.Application
import com.example.tasuku.data.AppContainer
import com.example.tasuku.data.DefaultAppContainer

class TasukuApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this.getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE))
    }
}