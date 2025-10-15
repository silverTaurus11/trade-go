package com.silvertaurus.trader_go.data.remote

import com.silvertaurus.trader_go.data.model.AssetsResponse
import com.silvertaurus.trader_go.data.model.HistoryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinCapApi {
    @GET("assets")
    suspend fun getAssets(
        @Query("ids") ids: String? = null,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): AssetsResponse

    @GET("assets/{id}/history")
    suspend fun getAssetHistory(
        @Path("id") id: String,
        @Query("interval") interval: String = "m15",
        @Query("start") start: Long,
        @Query("end") end: Long
    ): HistoryResponse
}