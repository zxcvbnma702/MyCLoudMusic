package com.example.lib_network.okhttp.response

import android.os.Handler
import android.os.Looper
import com.example.lib_network.okhttp.exception.OkHttpException
import com.example.lib_network.okhttp.listener.DisposeDataHandle
import com.example.lib_network.okhttp.listener.DisposeDataListener
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * @author:SunShibo
 * @date:2022-05-30 20:46
 * @feature: json解析
 */
open class CommonJsonCallback(handle: DisposeDataHandle) : Callback {

    /**
     * the logic layer exception, may alter in different app
     */
    protected val RESULT_CODE = "ecode" // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误

    protected val RESULT_CODE_VALUE = 0
    protected val ERROR_MSG = "emsg"
    private val EMPTY_MSG = ""

    /**
     * the java layer exception, do not same to the logic error
     */
    private val NETWORK_ERROR = -1 // the network relative error
    private val JSON_ERROR = -2 // the JSON relative error
    private val OTHER_ERROR = -3 // the unknown error

    /**
     * 将其它线程的数据转发到UI线程
     */
    private var mDeliveryHandler: Handler? = null

    private var mListener: DisposeDataListener? = null
    private var mClass: Class<*>? = null

    init {
        this.mClass = handle.mClass
        this.mListener = handle.mListener
        mDeliveryHandler = Handler(Looper.getMainLooper())
    }

    override fun onFailure(call: Call, e: IOException) {
        mDeliveryHandler?.post {
            mListener?.onFailure(OkHttpException(NETWORK_ERROR, e))
        }
    }

    override fun onResponse(call: Call, response: Response) {
        val result = response.body?.string()
        mDeliveryHandler?.post {
            handleResponse(result)
        }
    }

    private fun handleResponse(result: String?) {
        if ((result == null) or (result?.trim().equals(""))){
            mListener?.onFailure(OkHttpException(NETWORK_ERROR, EMPTY_MSG))
            return
        }

        try{
            //don`t need mClass
            if(mClass == null){
                mListener?.onSuccess(result!!)
            }else{
                val obj = Gson().fromJson(result.toString(), mClass);
                if (obj != null){
                    mListener?.onSuccess(obj)
                }else{
                    mListener?.onFailure(OkHttpException(JSON_ERROR, EMPTY_MSG))
                }
            }
        }catch (e: Exception){
            mListener?.onFailure(OkHttpException(OTHER_ERROR, EMPTY_MSG))
        }
    }
}