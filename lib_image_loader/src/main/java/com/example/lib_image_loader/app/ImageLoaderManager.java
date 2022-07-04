package com.example.lib_image_loader.app;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.lib_image_loader.R;
import com.example.lib_image_loader.image.CustomRequestListener;
import com.example.lib_image_loader.image.Utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

/**
 * 图处加载类，外界唯一调用类,直持为view,notifaication,appwidget加载图片
 */
public class ImageLoaderManager {

    private ImageLoaderManager() {
    }

    public static ImageLoaderManager getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final ImageLoaderManager instance = new ImageLoaderManager();
    }

    /**
     * 为notification加载图
     */
    public void displayImageForNotification(Context context, RemoteViews rv, int id,
                                            Notification notification, int NOTIFICATION_ID, String url) {
        this.displayImageForTarget(context,
                initNotificationTarget(context, id, rv, notification, NOTIFICATION_ID), url);
    }

    public void displayImageForNotification(Context context, RemoteViews rv, int id,
                                            Notification notification, int NOTIFICATION_ID, Bitmap bitmap) {
        this.displayImageForTarget(context,
                initNotificationTarget(context, id, rv, notification, NOTIFICATION_ID), bitmap);
    }

    /**
     * 不带回调的加载
     */
    public void displayImageForView(ImageView imageView, String url) {
        this.displayImageForView(imageView, url, null);
    }

    /**
     * 带回调的加载图片方法
     */
    public void displayImageForView(ImageView imageView, String url,
                                    CustomRequestListener requestListener) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(withCrossFade())
                .into(imageView);
    }

    /**
     * 带回调的加载图片方法
     */
    public void displayImageForCircle(final ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(final Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    public void displayImageForViewGroup(final ViewGroup group, String url) {
        Glide.with(group.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new SimpleTarget<Bitmap>() {//设置宽高
                    @SuppressLint("CheckResult")
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource,
                                                @Nullable Transition<? super Bitmap> transition) {
                        final Bitmap res = resource;
                        Observable.just(resource)
                                .map(bitmap -> (Drawable) new BitmapDrawable(
                                        Utils.doBlur(res, 100, true)
                                ))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(group::setBackground);
                    }
                });
    }

    /**
     * 为非view加载图片
     */
    private void displayImageForTarget(Context context, Target target, String url) {
        this.displayImageForTarget(context, target, url, null);
    }

    /**
     * 为非view加载图片
     */
    private void displayImageForTarget(Context context, Target target, Bitmap bitmap) {
        this.displayImageForTarget(context, target, bitmap, null);
    }

    /**
     * 为非view加载图片
     */
    private void displayImageForTarget(Context context, Target target, String url,
                                       CustomRequestListener requestListener) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(withCrossFade())
                .fitCenter()
                .listener(requestListener)
                .into(target);
    }

    private void displayImageForTarget(Context context, Target target, Bitmap bitmap,
                                       CustomRequestListener requestListener) {
        Glide.with(context)
                .asBitmap()
                .load(bitmap)
                .apply(initCommonRequestOption())
                .transition(withCrossFade())
                .fitCenter()
                .listener(requestListener)
                .into(target);
    }

    /*
     * 初始化Notification Target
     */
    private NotificationTarget initNotificationTarget(Context context, int id, RemoteViews rv,
                                                      Notification notification, int NOTIFICATION_ID) {
        return new NotificationTarget(context, id, rv, notification, NOTIFICATION_ID);
    }

    @SuppressLint("CheckResult")
    private RequestOptions initCommonRequestOption() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.b4y)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .priority(Priority.NORMAL);
        return options;
    }
}
