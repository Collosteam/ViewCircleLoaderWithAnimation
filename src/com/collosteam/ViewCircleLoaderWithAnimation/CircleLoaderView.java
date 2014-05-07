package com.collosteam.ViewCircleLoaderWithAnimation;

import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created
 */
public class CircleLoaderView extends View {
    public static final String TAG = CircleLoaderView.class.getSimpleName();
    private int height = 180;
    private int width = 180;

    public interface AnimationListener {

        public void startAnimation(CircleLoaderView circleLoaderView);

        public void onProgressAnimation(CircleLoaderView circleLoaderView, float progress);

        public void stopAnimation(CircleLoaderView circleLoaderView);

    }

    List<AnimationListener> animationListeners;

    public boolean addAnimationListener(AnimationListener listener) {
        if (animationListeners == null || !animationListeners.contains(listener)) {
            animationListeners = new ArrayList<AnimationListener>();
            animationListeners.add(listener);
            return true;
        }
        return false;
    }

    public interface PersentChangeListener {
        public void onChange(int persent);
    }

    public boolean addPersentChangeListener(PersentChangeListener listener) {
        if (listeners == null || !listeners.contains(listener)) {
            listeners = new ArrayList<PersentChangeListener>();
            listeners.add(listener);
            return true;
        }
        return false;
    }

    private List<PersentChangeListener> listeners;

    public int getPersent() {
        persent = (int) (endAngle - startAngle) * 100 / 360;

        return persent;
    }

    private int persent = 0;

    public CircleLoaderView(Context context) {
        super(context);
        init();
    }

    public CircleLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleLoaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

        //   this.invalidate();
    }

    // Create Grid Menu Circle
    public Bitmap createCircle(int color, int width) {

        Bitmap output = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(width / 2, width / 2, (float) (width / 2.25), paint);
        return output;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = Math.min(getLayoutParams().height, getLayoutParams().width);
        height = width;

        RectF rectF = new RectF(0, 0, width, height);
        canvas.clipRect(rectF);

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);

        RectF rectFinner = new RectF(strokeWidth, strokeWidth, width - strokeWidth, height - strokeWidth);
        Paint paintInner = new Paint();
        paintInner.setColor(Color.BLACK);
        paintInner.setAntiAlias(true);
        Path path = new Path();
        path.addArc(rectFinner, startAngle + 270, posetive ? endAngle : (-endAngle));
        canvas.drawPath(path, paint);
        canvas.drawArc(rectFinner, 0,360,true,paintInner);

    }

    private float startAngle = 0;
    private float endAngle = 0;

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        this.postInvalidate();
    }

    private float strokeWidth = 15.0f;

    public void setColor(int color) {
        if (this.color != color) {
            this.color = color;
            this.postInvalidate();
        }
    }

    int color = Color.WHITE;


    public void setStartAngle(float startAngle) {
        if (this.startAngle != startAngle) {
            this.startAngle = startAngle;
            this.postInvalidate();
            if (listeners != null) {

                for (PersentChangeListener listener : listeners) {
                    listener.onChange(getPersent());
                }
            }
        }
    }

    public void setEndAngle(float endAngle) {
        if (this.endAngle != endAngle) {
            this.endAngle = endAngle;
            if (listeners != null)
                for (PersentChangeListener listener : listeners) {
                    listener.onChange(getPersent());
                }
            this.postInvalidate();
        }
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    private boolean animation;
    private float i = 0;
    private long time = 1000L;
    private float DEFAULT_COEFICIENT = 4f;
    private float coeficient = 4f;//3.6f;

    public void startAnimation(long time) {
        setStartAngle(0);
        i = 0;
        {
            if (animationListeners != null)
                for (AnimationListener listener : animationListeners) {
                    listener.startAnimation(this);
                }
            this.time = time;
            if (endAngle > 270)
                coeficient = DEFAULT_COEFICIENT * 2.5f;
            else if (endAngle > 180)
                coeficient = DEFAULT_COEFICIENT * 2;
            else if (endAngle > 90)
                coeficient = DEFAULT_COEFICIENT *1.5f;
            else if (endAngle>45)
                coeficient = DEFAULT_COEFICIENT /2;
            else
                coeficient = DEFAULT_COEFICIENT /4;
            animate((int) endAngle);

         //   Log.d(TAG, "Delay : " + time / (endAngle / coeficient));
        }
    }

    public void setPosetive(boolean posetive) {
        this.posetive = posetive;
    }

    boolean posetive = true;

    private void animate(final int toAngle) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setEndAngle(i);
                final float curentI = i;
                if ((i += coeficient) <= toAngle) {
                    if (animationListeners != null)
                        animate(toAngle);
                        for (AnimationListener listener : animationListeners) {
                            listener.onProgressAnimation(CircleLoaderView.this, curentI);
                        }

             } else{
                    if(i != toAngle)
                        setEndAngle(toAngle);
                     if (animationListeners != null)
                        for (AnimationListener listener : animationListeners) {
                            listener.stopAnimation(CircleLoaderView.this);
                        }
                }
            }
        }, time / (toAngle / coeficient) < 1 ? 1 : (long) (time / (toAngle / coeficient)));
    }


}
