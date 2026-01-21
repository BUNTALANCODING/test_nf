package com.nf.test.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import business.core.AppDataStore
import common.Context
import di.initKoinOnce
import org.koin.android.ext.android.get
import presentation.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val platformContext = Context(this)

        initKoinOnce(platformContext)

        val appDataStore: AppDataStore = get()

        setContent {
            App(
                context = platformContext,
                appDataStore = appDataStore
            )
        }
    }
}