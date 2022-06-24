package com.example.lib_network.okhttp.response

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.lib_network.okhttp.exception.OkHttpException
import com.example.lib_network.okhttp.listener.DisposeDataHandle
import com.example.lib_network.okhttp.listener.DisposeDownloadListener
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.properties.Delegates

/**
 * @author:SunShibo
 * @date:2022-05-30 21:39
 * @feature:
 */
class CommonFileCallback(handle: DisposeDataHandle) : Callback{

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
    private val IO_ERROR = -3 // the unknown error

    /**
     * 将其它线程的数据转发到UI线程
     */
    private var mDeliveryHandler: Handler? = null

    private var mListener: DisposeDownloadListener? = null

    /**
     * 进度数值
     */
    private var PROGRESS_MESSAGE = 0x01

    /**
     * 文件路径
     */
    private lateinit var mFilePath: String

    /**
     * 当前进度
     */
    private var mProgress by Delegates.notNull<Int>()

    init{
        this.mListener = handle.mListener as DisposeDownloadListener?
        this.mFilePath = handle.mSource.toString()
        mDeliveryHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    PROGRESS_MESSAGE -> mListener!!.onProgress(msg.obj as Int)
                }
            }
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        mDeliveryHandler?.post {
            mListener?.onFailure(OkHttpException(NETWORK_ERROR, e))
        }
    }

    override fun onResponse(call: Call, response: Response) {
        val file: File? = handleResponse(response)
        if(file != null){
            mListener?.onSuccess(file)
        }else{
            mListener?.onFailure(OkHttpException(IO_ERROR, EMPTY_MSG))
        }
    }

    private fun handleResponse(response: Response?): File?{
        if(response == null){
            return null
        }

        var inputStream: InputStream? = null
        var file: File?
        var fos: FileOutputStream? = null
        val buffer = ByteArray(2048)
        var length: Int
        var currentLength = 0
        val sumLength: Double

        try {
            checkLocalFilePath(mFilePath)
            file = File(mFilePath)
            fos = FileOutputStream(file)

            inputStream = response.body!!.byteStream()
            sumLength = response.body!!.contentLength().toDouble()

            while (inputStream.read(buffer).also { length = it } != -1) {
                fos.write(buffer, 0, length)
                currentLength += length
                mProgress = (currentLength / sumLength * 100).toInt()
                mDeliveryHandler!!.obtainMessage(PROGRESS_MESSAGE, mProgress)
                    .sendToTarget()
            }

            fos.flush()
        } catch (e: Exception) {
            file = null
        } finally {
            try {
                fos?.close()
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file
    }

    private fun checkLocalFilePath(localFilePath: String) {
        val path = File(localFilePath.substring(0, localFilePath.lastIndexOf("/") + 1))
        val file = File(localFilePath)
        if (!path.exists()) {
            path.mkdirs()
        }
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}