package com.silvertaurus.trader_go.data.remote.header

import com.silvertaurus.trader_go.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.COINCAP_API_KEY}")
            .build()
        return chain.proceed(request)
    }
}