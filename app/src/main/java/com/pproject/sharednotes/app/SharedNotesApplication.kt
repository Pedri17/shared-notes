package com.pproject.sharednotes.app

import android.app.Application
import com.pproject.sharednotes.data.AppContainer

class SharedNotesApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}