package com.dooit.app

import android.app.Application
import timber.log.Timber

class DooitApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}