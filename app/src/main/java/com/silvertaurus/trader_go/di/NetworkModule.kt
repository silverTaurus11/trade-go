package com.silvertaurus.trader_go.di

import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.silvertaurus.trader_go.BuildConfig
import com.silvertaurus.trader_go.data.local.LocalAssetDataSource
import com.silvertaurus.trader_go.data.local.database.AppDatabase
import com.silvertaurus.trader_go.data.remote.CoinCapApi
import com.silvertaurus.trader_go.data.remote.CoinCapWebSocketManager
import com.silvertaurus.trader_go.data.remote.RemoteAssetDataSource
import com.silvertaurus.trader_go.data.remote.header.AuthInterceptor
import com.silvertaurus.trader_go.data.repositoryimpl.AssetRepositoryImpl
import com.silvertaurus.trader_go.domain.repository.AssetRepository
import com.silvertaurus.trader_go.domain.utils.DefaultDispatcherProvider
import com.silvertaurus.trader_go.domain.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setStrictness(Strictness.LENIENT)
        .create()

    @Provides
    @Singleton
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        return ChuckerInterceptor.Builder(context)
            .collector(collector)
            .maxContentLength(250_000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor { message ->
            android.util.Log.d("HTTPS", message)
        }
        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return interceptor
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor = AuthInterceptor()

    @Provides
    @Singleton
    fun provideOkHttp(
        authInterceptor: AuthInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, ok: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://rest.coincap.io/v3/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(ok)
        .build()

    @Provides
    @Singleton
    fun provideCoinCapApi(retrofit: Retrofit): CoinCapApi = retrofit.create(CoinCapApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "crypto_db").build()

    @Provides
    @Singleton
    fun provideCoinCapWebSocket(ok: OkHttpClient, gson: Gson): CoinCapWebSocketManager =
        CoinCapWebSocketManager(ok, gson)

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        api: CoinCapApi,
        ws: CoinCapWebSocketManager
    ): RemoteAssetDataSource =
        RemoteAssetDataSource(api, ws)

    @Provides
    @Singleton
    fun provideLocalDataSource(db: AppDatabase): LocalAssetDataSource =
        LocalAssetDataSource(db.assetDao(), db.watchlistDao())

    @Provides
    @Singleton
    fun provideAssetRepository(
        remote: RemoteAssetDataSource,
        local: LocalAssetDataSource
    ): AssetRepository = AssetRepositoryImpl(remote, local)

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

}
