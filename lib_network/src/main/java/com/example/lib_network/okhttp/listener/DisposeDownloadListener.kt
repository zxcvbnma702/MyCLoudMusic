package com.example.lib_network.okhttp.listener

/**
 * @author:SunShibo
 * @date:2022-05-30 21:46
 * @feature:
 */
interface DisposeDownloadListener: DisposeDataListener {

    fun onProgress(progress: Int)
}