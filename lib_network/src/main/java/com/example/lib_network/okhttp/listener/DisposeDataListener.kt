package com.example.lib_network.okhttp.listener

/**
 * @author:SunShibo
 * @date:2022-05-30 20:50
 * @feature:
 */
interface DisposeDataListener{

    /**
     * 请求事物回调成功处理
     */
    fun onSuccess(responseObj: Any)

    /**
     * 请求事物回调失败处理
     */
    fun onFailure(reasonObj: Any)
}