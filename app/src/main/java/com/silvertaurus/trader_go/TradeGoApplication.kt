package com.silvertaurus.trader_go

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.silvertaurus.trader_go.data.remote.CoinCapWebSocketManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class TradeGoApplication : Application() {
    @Inject
    lateinit var webSocketManager: CoinCapWebSocketManager

    override fun onCreate() {
        super.onCreate()
        val lifecycle = ProcessLifecycleOwner.get().lifecycle
        lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        webSocketManager.connect()
                    }

                    Lifecycle.Event.ON_STOP -> {
                        webSocketManager.close()
                    }

                    else -> {}
                }
            }
        )
    }
}