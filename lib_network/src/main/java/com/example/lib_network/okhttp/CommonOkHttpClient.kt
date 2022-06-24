package com.example.lib_network.okhttp

import com.example.lib_network.okhttp.listener.DisposeDataHandle
import com.example.lib_network.okhttp.response.CommonFileCallback
import com.example.lib_network.okhttp.response.CommonJsonCallback
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * @author:SunShibo
 * @date:2022-05-30 22:15
 * @feature:
 */

object CommonOkHttpClient {

    private const val TIME_OUT = 30
    private var mOkHttpClient: OkHttpClient? = null

    fun getOkHttpClient(): OkHttpClient? {
        return mOkHttpClient
    }

    /**
     * 通过构造好的Request,Callback去发送请求
     */
    fun get(request: Request?, handle: DisposeDataHandle?): Call {
        val call = mOkHttpClient!!.newCall(request!!)
        call.enqueue(CommonJsonCallback(handle!!))
        return call
    }

    fun post(request: Request?, handle: DisposeDataHandle?): Call {
        val call = mOkHttpClient!!.newCall(request!!)
        call.enqueue(CommonJsonCallback(handle!!))
        return call
    }

    fun downloadFile(request: Request?, handle: DisposeDataHandle?): Call {
        val call = mOkHttpClient!!.newCall(request!!)
        call.enqueue(CommonFileCallback(handle!!))
        return call
    }

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.hostnameVerifier { _, _ -> true }

        /**
         * 为所有请求添加请求头
         */
        okHttpClientBuilder.addInterceptor(Interceptor {
            val request =
                it.request().newBuilder().addHeader("User-Agent", "Imooc-Mobile") // 标明发送本次请求的客户端
                    .build()
            it.proceed(request)
        })

//        okHttpClientBuilder.cookieJar(SimpleCookieJar())
        okHttpClientBuilder.connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        okHttpClientBuilder.followRedirects(true)

//        /**
//         * trust all the https point
//         */
//        okHttpClientBuilder.sslSocketFactory(
//            HttpsUtils.initSSLSocketFactory(),
//            HttpsUtils.initTrustManager()
//        )
        mOkHttpClient = okHttpClientBuilder.build()
    }
}