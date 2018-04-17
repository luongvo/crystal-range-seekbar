package com.crystal.crystalrangeseekbar.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by luongvo on 4/16/18.
 */
abstract class Seekbar extends View {

    public Seekbar(Context context) {
        super(context);
    }

    public Seekbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Seekbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Init Shadow
//        if (attributes.getBoolean(R.styleable.CircularImageView_civ_shadow, false)) {
//            shadowRadius = DEFAULT_SHADOW_RADIUS;
//            drawShadow(attributes.getFloat(R.styleable.CircularImageView_civ_shadow_radius, shadowRadius), attributes.getColor(R.styleable.CircularImageView_civ_shadow_color, shadowColor));
        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
        paintBorder.setColor(Color.RED);

        drawShadow(shadowRadius, Color.parseColor("#6f6f6f"));
//        }
    }

    @SuppressLint("NewApi")
    protected static Bitmap getBitmap(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof VectorDrawableCompat || drawable instanceof VectorDrawable) {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);

                return bitmap;
            } else {
                throw new IllegalArgumentException("unsupported drawable type");
            }
        }
        return null;
    }

    protected Bitmap addShadow(final Bitmap bm, final int dstHeight, final int dstWidth, int color, int size, float dx, float dy) {
        final Bitmap mask = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ALPHA_8);

        final Matrix scaleToFit = new Matrix();
        final RectF src = new RectF(0, 0, bm.getWidth(), bm.getHeight());
        final RectF dst = new RectF(0, 0, dstWidth - dx, dstHeight - dy);
        scaleToFit.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);

        final Matrix dropShadow = new Matrix(scaleToFit);
        dropShadow.postTranslate(dx, dy);

        final Canvas maskCanvas = new Canvas(mask);
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskCanvas.drawBitmap(bm, scaleToFit, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        maskCanvas.drawBitmap(bm, dropShadow, paint);

        final BlurMaskFilter filter = new BlurMaskFilter(size, BlurMaskFilter.Blur.NORMAL);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setMaskFilter(filter);
        paint.setFilterBitmap(true);

        final Bitmap ret = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
        final Canvas retCanvas = new Canvas(ret);
        retCanvas.drawBitmap(mask, 0, 0, paint);
        retCanvas.drawBitmap(bm, scaleToFit, null);
        mask.recycle();
        return ret;

//        Paint shadowPaint = new Paint();
//        shadowPaint.setAntiAlias(true);
//        shadowPaint.setColor(Color.WHITE);
//        shadowPaint.setTextSize(45.0f);
//        shadowPaint.setStrokeWidth(2.0f);
//        shadowPaint.setStyle(Paint.Style.STROKE);
//        shadowPaint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//
//        final Bitmap ret = Bitmap.createBitmap(dstWidth + 50, dstHeight + 50, Bitmap.Config.ARGB_8888);
//        final Canvas canvas = new Canvas(ret);
////        canvas.drawText("http://android-er.blogspot.com/", 10, 10, shadowPaint);
//        canvas.drawBitmap(bm, 10, 10, shadowPaint);
//
//        return ret;
    }

    protected Paint paintBorder;
    protected int shadowRadius = 20;

    protected void drawShadow(float shadowRadius, int shadowColor) {
//        this.shadowRadius = shadowRadius;
//        this.shadowColor = shadowColor;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
        }
        paintBorder.setShadowLayer(shadowRadius, 0.0f, shadowRadius / 2, shadowColor);

    }
}
