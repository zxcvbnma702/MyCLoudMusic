package com.example.cloudmusic.net

import com.example.cloudmusic.view.login.entity.User
import com.example.lib_network.okhttp.CommonOkHttpClient
import com.example.lib_network.okhttp.listener.DisposeDataHandle
import com.example.lib_network.okhttp.listener.DisposeDataListener
import com.example.lib_network.okhttp.request.CommonRequest
import com.example.lib_network.okhttp.request.RequestParams
import java.net.Authenticator

/**
 * @author:SunShibo
 * @date:2022-05-31 21:54
 * @feature:
 */
object RequestCenter {

    object HttpConstants{
        private val ROOT_URL = "https://mock.apifox.cn/m1/1063673-0-default"

        val HOME_RECOMMAND = ROOT_URL + "product/home/"
        val LOGIN = ROOT_URL + "user/login_phone"
    }


    private fun postRequest(url: String, params: RequestParams, listener: DisposeDataListener, clazz: Class<*>) =
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params), DisposeDataHandle(listener, clazz))

    fun login(listener: DisposeDataListener){
        val params = RequestParams()
        params.put("phone", "13630388538")
        params.put("pwd", "999")
        postRequest(HttpConstants.LOGIN, params, listener, User::class.java)
    }
}