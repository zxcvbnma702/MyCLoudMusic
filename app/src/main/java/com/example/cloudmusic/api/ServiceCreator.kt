package com.example.cloudmusic.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author:SunShibo
 * @date:2022-07-04 10:12
 * @feature:
 */
object ServiceCreator {
    private const val BASE_URL = "http://api.tianapi.com/generalnews/"//基础URL
    //构建retrofit对象
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    //对外界提供的api接口
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    //利用内联和泛型实化简化写法
    inline fun <reified T> create(): T = create(T::class.java)
}