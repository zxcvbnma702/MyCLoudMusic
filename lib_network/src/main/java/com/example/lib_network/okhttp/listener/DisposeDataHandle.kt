package com.example.lib_network.okhttp.listener

/**
 * @author:SunShibo
 * @date:2022-05-30 20:58
 * @feature:
 */
class DisposeDataHandle {

    /**
     * 逻辑监听器
     */
    var mListener: DisposeDataListener? = null

    /**
     * json解析类
     */
    var mClass: Class<*>? = null

    /**
     * 文件保存路径
     */
    var mSource: String? = null

    constructor(listener: DisposeDataListener?) {
        mListener = listener
    }

    constructor(listener: DisposeDataListener?, clazz: Class<*>?) {
        mListener = listener
        mClass = clazz
    }

    constructor(listener: DisposeDataListener?, source: String?) {
        mListener = listener
        mSource = source
    }
}