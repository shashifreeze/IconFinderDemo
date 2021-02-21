package com.iconfinderdemo.network

import com.iconfinderdemo.network.apiresponse.IconResponse
import com.iconfinderdemo.network.apiresponse.IconSetResponse
import io.reactivex.Single
import retrofit2.Call

class IconApiService {

    private var api: IconApi = RetrofitClient.getInstance().getIconApi()

    fun getIconSets(count: Int,after:Int): Single<IconSetResponse> {
        return api.getIconSets(count,after)
    }
    fun getIconSets(count: Int): Single<IconSetResponse> {
        return api.getIconSets(count)
    }

    fun getAllIconsOfIconSet(iconSetId: Int,count:Int): Single<IconResponse>{
        return api.getIconsListOfIconSet(iconSetId,count)
    }

    fun getAllIconsOfIconSet(iconSetId: Int): Single<IconResponse>{
        return api.getIconsListOfIconSet(iconSetId)
    }

}