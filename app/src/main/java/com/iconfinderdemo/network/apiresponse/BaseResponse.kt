
package com.iconfinderdemo.network.apiresponse;
import com.google.gson.annotations.SerializedName
open class BaseResponse (

    @SerializedName("total_count")
    var totalCount: Int = 0
)