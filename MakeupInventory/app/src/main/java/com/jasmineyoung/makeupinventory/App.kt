package com.jasmineyoung.makeupinventory

import android.app.Application
import android.util.Log

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("BSU", "Loaded Application")
    }
}