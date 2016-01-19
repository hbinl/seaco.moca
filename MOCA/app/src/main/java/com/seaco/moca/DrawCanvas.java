package com.seaco.moca;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Calendar;

public class DrawCanvas extends View {
    private static final float MINP = 0.25f;
    private static final float MAXP = 0.75f;
    private Context con;
    private Bitmap  mBitmap;
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;
    private Paint   mPaint;

    private ArrayList<Long> strokeTimes;
    private ArrayList<Float> points;


    public DrawCanvas(Context c, AttributeSet as) {
        super(c, as);
        con = c;

        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);

        strokeTimes = new ArrayList<Long>();
        points = new ArrayList<Float>();
    }

    public void saveImageIntoSessionWithOverlay(XMLHandlerSession _session, String name) {

        Bitmap drawing = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(drawing);
        draw(canvas);


        Bitmap bitmapBase;
        if (_session.localeInt == 1) {
            bitmapBase = BitmapFactory.decodeResource(getResources(), R.drawable.moca_visuo_ms);
        } else if (_session.localeInt == 2) {
            bitmapBase = BitmapFactory.decodeResource(getResources(), R.drawable.moca_visuo_cn);
        } else {
            bitmapBase = BitmapFactory.decodeResource(getResources(), R.drawable.moca_visuo_en);
        }

        Bitmap bitmapBaseScaled = Bitmap.createScaledBitmap(bitmapBase, getWidth(), getHeight(), false);
        Canvas canvasBase = new Canvas(bitmapBaseScaled.copy(Bitmap.Config.ARGB_8888, true));
        canvasBase.drawBitmap(bitmapBaseScaled, 0, 0, null);


        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        Bitmap whiteBg = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvasBg = new Canvas(whiteBg);
        canvasBg.drawColor(0xffffffff);
        canvasBg.drawBitmap(drawing, 0,0,p);
        canvasBg.drawBitmap(bitmapBaseScaled, 0,0,p);

        _session.saveImage(whiteBg, name);

    }

    public void saveImageIntoSession(XMLHandlerSession _session, String name) {
        Bitmap bmpBase = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmpBase);
        draw(canvas);
        _session.saveImage(bmpBase, name);
    }

    public void clear() {
        mCanvas.drawColor(0xFFFFFFFF);
        invalidate();


    }

    public void setColor(int hex) {
        mPaint.setColor(hex);
    }

    public void setErase() {
        mPaint.setColor(0xFF333333);
        mPaint.setStrokeWidth(80);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public void setPaint() {
        mPaint.setColor(0xFF000000);
        mPaint.setStrokeWidth(5);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFFFFFFF);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    public ArrayList<Long> getStrokeTimes() {
        return strokeTimes;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                points.add(x);
                points.add(y);
                strokeTimes.add(Calendar.getInstance().getTimeInMillis());
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                points.add(x);
                points.add(y);
                strokeTimes.add(Calendar.getInstance().getTimeInMillis());
                touch_up();
                invalidate();
                break;
        }
        return true;
    }


    public ArrayList<Float> getPoints() {
        return points;
    }
}
