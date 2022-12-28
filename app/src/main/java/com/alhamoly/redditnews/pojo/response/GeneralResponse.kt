package com.alhamoly.redditnews.pojo.response

import com.alhamoly.redditnews.utils.Constants

data class GeneralResponse(
    val code: Int = Constants.Codes.SUCCESSES_CODE,
    val `data`: Any=Any(),
    val msg: String = "",
    val success: Boolean = true
)