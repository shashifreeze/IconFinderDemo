package com.iconfinderdemo.network

import com.iconfinderdemo.model.IconSet
import com.iconfinderdemo.network.apiresponse.IconResponse
import com.iconfinderdemo.network.apiresponse.IconSetResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IconApi {

    @GET("iconsets")
    fun getIconSets(@Query("count") count: Int,@Query("after") after: Int ): Single<IconSetResponse>

    @GET("iconsets")
    fun getIconSets(@Query("count") count: Int ): Single<IconSetResponse>

    @GET("iconsets/{iconset_id}/icons")
    fun getIconsListOfIconSet(
        @Path("iconset_id") iconSetId: Int,
        @Query("count") count: Int
    ): Single<IconResponse>

    @GET("icons/search")
    fun search(
        @Query("query") query: String,
        @Query("count") count: Int,
        @Query("premium") premium: Boolean
    ): Call<IconResponse>

}