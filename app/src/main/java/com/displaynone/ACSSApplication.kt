package com.displaynone

import android.app.Application
import com.displaynone.acss.components.auth.models.user.UserServiceST

class ACSSApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        UserServiceST.createInstance(this)
    }
}
