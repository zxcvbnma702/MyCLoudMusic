package com.example.cloudmusic.api

import com.example.cloudmusic.model.News
import com.example.cloudmusic.model.NewsBean
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * @author:SunShibo
 * @date:2022-07-04 10:19
 * @feature:
 */
interface NewService {
    @GET("index?key=fa4b8de7bd85373df2e8787714774fb3&num=30&rand=1")
    fun get(): Call<NewsBean>
}