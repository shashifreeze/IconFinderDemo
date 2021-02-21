package com.iconfinderdemo.network.apiresponse;

import com.iconfinderdemo.model.Category

data class CategoryResponse(
    val categories: List<Category>
) : BaseResponse()