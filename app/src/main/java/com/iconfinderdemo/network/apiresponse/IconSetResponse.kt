package com.iconfinderdemo.network.apiresponse;
import com.google.gson.annotations.SerializedName
import com.iconfinderdemo.model.IconSet

data class IconSetResponse(
    @SerializedName("iconsets")
    val iconSets: List<IconSet>
): BaseResponse()