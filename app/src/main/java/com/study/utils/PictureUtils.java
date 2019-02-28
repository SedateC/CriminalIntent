package com.study.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path,int destWidth,int destHeight){
        // Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        // Figure out how much to scale down by
        int inSampleSize = 1;
        if (srcHeight>destHeight||srcWidth>destWidth){
            float heightScale = srcHeight/destHeight;
            float widthScale = srcWidth/destWidth;
            inSampleSize = Math.round(heightScale>widthScale?heightScale:widthScale);
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        return  BitmapFactory.decodeFile(path,options);
    }
    //该方法先确认屏幕的尺寸，然后按此缩放图像。这样，就能保证载入的 ImageView 永远不会
    //过大。无论如何，这是一个比较保守的估算，但能解决问题。
    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);//草赋值语句
        return getScaledBitmap(path,size.x,size.y);
    }
}
