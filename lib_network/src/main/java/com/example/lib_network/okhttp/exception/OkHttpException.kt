package com.example.lib_network.okhttp.exception

/**
 * @author:SunShibo
 * @date:2022-05-30 21:04
 * @feature:自定义异常类,返回ecode,emsg到业务层
 */

data class OkHttpException(var ecode: Int, var emsg: Any) : Exception() {
    companion object {
        private const val serialVersionUID = 1L
    }
}