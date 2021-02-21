package com.iconfinderdemo.network.apiresponse;
import com.iconfinderdemo.model.Icon
data class IconResponse(
    val icons: List<Icon>
) : BaseResponse()