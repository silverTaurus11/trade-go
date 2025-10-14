package com.silvertaurus.trader_go.data.remote

import android.util.Log
import androidx.lifecycle.LifecycleObserver
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.silvertaurus.trader_go.BuildConfig
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(DelicateCoroutinesApi::class)
@Singleton
class CoinCapWebSocketManager @Inject constructor(
    private val okHttp: OkHttpClient,
    private val gson: Gson
): LifecycleObserver {
    private var webSocket: WebSocket? = null

    private val _prices = MutableSharedFlow<Map<String, Double>>(replay = 1)
    val prices: SharedFlow<Map<String, Double>> = _prices.asSharedFlow()

    private var isConnected = false

    fun connect() {
        if (isConnected) return
        val request = Request.Builder()
            .url("wss://wss.coincap.io/prices?assets=ALL&apiKey=${BuildConfig.COINCAP_API_KEY}")
            .build()

        webSocket = okHttp.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                isConnected = true
                Log.d("WS", "Connected ✅")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val type = object : TypeToken<Map<String, Double>>() {}.type
                    val map: Map<String, Double> = gson.fromJson(text, type)
                    GlobalScope.launch(Dispatchers.IO) {
                        _prices.emit(map)
                    }
                    val sample = map.entries.take(5).joinToString { "${it.key}=${it.value}" }
                    Log.v("WebSocket", "📨 Received update: $sample ...")
                } catch (e: Exception) {
                    Log.e("WS", "parse error: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                isConnected = false
                Log.e("WS", "failure: ${t.message}")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                isConnected = false
                Log.d("WS", "closed $reason")
            }
        })
    }

    fun close() {
        webSocket?.close(1000, "App closed")
        webSocket = null
        isConnected = false
    }
}