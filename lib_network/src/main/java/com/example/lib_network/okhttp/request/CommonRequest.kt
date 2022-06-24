package com.example.lib_network.okhttp.request

import okhttp3.*
import okhttp3.Headers.Companion.headersOf
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 * @author:SunShibo
 * @date:2022-05-29 23:04
 * @feature: 对外提供post,文件上传请求
 */
object CommonRequest {
    /**
     * 文件上传
     */
    private val FILE_TYPE = "application/octet-stream".toMediaTypeOrNull()

    /*
    创建POST请求
     */
    fun createPostRequest(url: String, params: RequestParams?): Request =
        createPostRequest(url, params, null)

    fun createPostRequest(url: String, params: RequestParams?, headers: RequestParams?): Request {
        //参数遍历
        val mFromBodyBuilder = FormBody.Builder()
        if (params != null) {
            for (entry in params.urlParams.entries) {
                mFromBodyBuilder.add(entry.key, entry.value)
            }
        }
        //请求头遍历
        val mHeaderBuilder = Headers.Builder()
        if (headers != null) {
            for (entry in headers.urlParams.entries) {
                mHeaderBuilder.add(entry.key, entry.value)
            }
        }

        return Request.Builder()
            .url(url)
            .headers(mHeaderBuilder.build())
            .post(mFromBodyBuilder.build())
            .build()
    }

    fun createGetRequest(url: String, params: RequestParams?): Request =
        createGetRequest(url, params, null)

    fun createGetRequest(url: String, params: RequestParams?, headers: RequestParams?): Request{
        val urlBuilder = StringBuilder(url).append("?")

        if (params != null) {
            for (entry in params.urlParams.entries) {
                urlBuilder.append(entry.key).append("=").append(entry.value)
            }
        }

        val mHeaderBuilder = Headers.Builder()
        if (headers != null) {
            for (entry in headers.urlParams.entries) {
                mHeaderBuilder.add(entry.key, entry.value)
            }
        }

        return Request.Builder()
            .url(urlBuilder.substring(0, urlBuilder.length - 1))
            .headers(mHeaderBuilder.build())
            .get()
            .build()
    }

    fun createMultiPostRequest(url: String, params: RequestParams?): Request? {
        val requestBody = MultipartBody.Builder()
        requestBody.setType(MultipartBody.FORM)

        if (params != null) {
            for (entry in params.fileParams.entries) {

                if (entry.value is File) {
                    requestBody.addPart(
                        headersOf("Content-Disposition", "form-data; name=\"${entry.key}\""),
                        (entry.value as File?)!!.asRequestBody(FILE_TYPE)
                    )
                } else if (entry.value is String) {
                    requestBody.addPart(
                        headersOf("Content-Disposition", "form-data; name=\"${entry.key}\""),
                        (entry.value as String?)!!.toRequestBody(null)
                    )
                }
            }
        }
        return Request.Builder().url(url).post(requestBody.build()).build()
    }
}