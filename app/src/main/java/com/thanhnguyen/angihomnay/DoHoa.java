package com.thanhnguyen.angihomnay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class DoHoa{

    /*
     * Draw image in circular shape Note: change the pixel size if you want
     * image small or large
     */
    public Bitmap getCircleBitmap(Bitmap bitmap)
    {
        Bitmap output;
        Canvas canvas = null;
        final int color = 0xffff0000;
        final Paint paint = new Paint();
        Rect rect = null;
        if (bitmap.getHeight() > 501)
        {
            output = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(output);
            rect = new Rect(0, 0, 500, 500);
        }
        else
        {
            //System.out.println("output            else =======");
            bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
            output = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(output);
            rect = new Rect(0, 0, 500, 500);
        }
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) 1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }



}