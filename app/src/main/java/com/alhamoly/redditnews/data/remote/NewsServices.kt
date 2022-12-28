package com.alhamoly.redditnews.data.remote

import com.alhamoly.redditnews.pojo.response.NewsResponse
import retrofit2.Response
import retrofit2.http.GET

interface NewsServices {

    @GET("kotlin/.json")
    suspend fun getNews(
    ): Response<NewsResponse>


}