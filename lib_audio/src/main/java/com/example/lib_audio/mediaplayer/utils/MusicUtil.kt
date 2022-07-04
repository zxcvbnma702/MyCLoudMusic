package com.example.lib_audio.mediaplayer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.os.Build
import android.provider.MediaStore
import com.example.lib_audio.R
import com.example.lib_audio.mediaplayer.model.AudioBean

/**
 * @author:SunShibo
 * @date:2022-07-03 16:19
 * @feature:
 */
object MusicUtil {
    fun getSong(context: Context): ArrayList<AudioBean> {
        val list = ArrayList<AudioBean>()
        val cursor = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
            MediaStore.Audio.Media.IS_MUSIC)
        if(cursor != null){
            while (cursor.moveToNext()){
                val audio = AudioBean()
                audio.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                audio.id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                audio.totalTime = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)).toString()
                audio.album = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)).toString()
                audio.albumInfo = if(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)) != null){
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                }else{
                    "unknown"
                }
                audio.url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    audio.author = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.AUTHOR))
                }else{
                    audio.author = if(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)) != null){
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    }else{
                        "unknown"
                    }
                }
                val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

                if (size > 1000 * 800) {
                    if (audio.name.contains("-")) {
                        val str = audio.name.split("-");
                        audio.author = str[0];
                        audio.name = str[1];
                    }
                    list.add(audio);
                }
            }
            cursor.close()
        }
        return list
    }

    /**
     * 获取专辑封面
     *
     * @param context 上下文
     * @param path    歌曲路径
     * @param type 1 Activity中显示，!1 通知栏中显示
     * @return
     */
    fun getAlbumPicture(context: Context, path: String, type: Int): Bitmap? {
        //歌曲检索
        val mmr = MediaMetadataRetriever()
        //设置数据源
        mmr.setDataSource(path)
        //获取图片数据
        val data = mmr.embeddedPicture
        var albumPicture: Bitmap? = null
        if (data != null) {
            //获取bitmap对象
            albumPicture = BitmapFactory.decodeByteArray(data, 0, data.size)
            //获取宽高
            val width = albumPicture.width
            val height = albumPicture.height
            // 创建操作图片用的Matrix对象
            val matrix = Matrix()
            // 计算缩放比例
            val sx = 120f / width
            val sy = 120f / height
            // 设置缩放比例
            matrix.postScale(sx, sy)
            // 建立新的bitmap，其内容是对原bitmap的缩放后的图
            albumPicture = Bitmap.createBitmap(albumPicture, 0, 0, width, height, matrix, false)
        } else {
            //从歌曲文件读取不出来专辑图片时用来代替的默认专辑图片
            albumPicture = if (type == 1) {
                //Activity中显示
                BitmapFactory.decodeResource(context.resources, R.mipmap.icon_music)
            } else {
                //通知栏显示
                BitmapFactory.decodeResource(context.resources, R.mipmap.icon_notification_default)
            }
            val width = albumPicture.width
            val height = albumPicture.height
            // 创建操作图片用的Matrix对象
            val matrix = Matrix()
            // 计算缩放比例
            val sx = 120f / width
            val sy = 120f / height
            // 设置缩放比例
            matrix.postScale(sx, sy)
            // 建立新的bitmap，其内容是对原bitmap的缩放后的图
            albumPicture = Bitmap.createBitmap(albumPicture, 0, 0, width, height, matrix, false)
        }
        return albumPicture
    }
}