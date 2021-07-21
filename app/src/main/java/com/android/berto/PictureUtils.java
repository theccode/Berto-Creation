package com.android.berto;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import java.io.InputStream;

public class PictureUtils {
    private static Bitmap getScaledBitmap(InputStream inputStream, int destWidth, int destHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream);

        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcWidth > destWidth || srcHeight > destHeight){
            float widthScale = srcWidth / destWidth;
            float heightScale = srcHeight /destHeight;
            inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }

        options  = new BitmapFactory.Options();;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeStream(inputStream);
    }
    public static Bitmap getScaledBitmap(InputStream inputStream, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(inputStream, size.x, size.y);
    }
}
