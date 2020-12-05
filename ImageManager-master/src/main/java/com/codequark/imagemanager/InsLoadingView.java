package com.codequark.imagemanager;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.graphics.Shader.TileMode.CLAMP;

public class InsLoadingView extends AppCompatImageView {
    private static final float ARC_WIDTH = 12;
    private static final float circleDia = 0.9f;
    private static final float strokeWidth = 0.025f;
    private static final float arcChangeAngle = 0.2f;

    private static final int sClickedColor = Color.LTGRAY;
    private static final int MIN_WIDTH = 300;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({StatusDef.LOADING, StatusDef.CLICKED, StatusDef.UNCLICKED})
    public @interface StatusDef {
        int LOADING = 0;
        int CLICKED = 1;
        int UNCLICKED = 2;
    }

    @StatusDef
    private int status = StatusDef.LOADING;
    private int rotateDuration = 10000;
    private int circleDuration = 2000;
    private int startColor = Color.parseColor("#FFF700C2");
    private int endColor = Color.parseColor("#FFFFD900");

    private float degrees;
    private float circleWidth;
    private float scale = 1f;

    private boolean firstCircle = true;

    private final ValueAnimator rotateAnim = ValueAnimator.ofFloat(0, 180, 360);
    private final ValueAnimator circleAnim = ValueAnimator.ofFloat(0, 360);
    private final ValueAnimator touchAnim = new ValueAnimator();

    private Paint bitmapPaint;
    private Paint trackPaint;

    private RectF bitmapRectF;
    private RectF trackRectF;

    public InsLoadingView(Context context) {
        super(context);
        init(context , null);
    }

    public InsLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public InsLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setCircleDuration(int circleDuration) {
        this.circleDuration = circleDuration;
        circleAnim.setDuration(circleDuration);
    }

    public void setRotateDuration(int rotateDuration) {
        this.rotateDuration = rotateDuration;
        rotateAnim.setDuration(rotateDuration);
    }

    public void setStatus(@StatusDef int status) {
        this.status = status;
    }

    @StatusDef
    public int getStatus() {
        return status;
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
        trackPaint = null;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
        trackPaint = null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        if(widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
            width = Math.min(widthSpecSize, heightSpecSize);
        } else {
            width = Math.min(widthSpecSize, heightSpecSize);
            width = Math.min(width, MIN_WIDTH);
        }

        //noinspection SuspiciousNameCombination
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPaints();
        initRectFs();
        canvas.scale(scale, scale, centerX(), centerY());
        drawBitmap(canvas);

        switch (status) {
            case StatusDef.LOADING:
                drawTrack(canvas, trackPaint);
                break;
            case StatusDef.UNCLICKED:
                drawCircle(canvas, trackPaint);
                break;
            case StatusDef.CLICKED:
                drawClickedircle(canvas);
                break;
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        if(visibility == View.VISIBLE) {
            startAnim();
        } else {
            endAnim();
        }

        super.onVisibilityChanged(changedView, visibility);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                startDownAnim();
                result = true;
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                startUpAnim();
                break;
            }
        }

        return super.onTouchEvent(event) || result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        bitmapRectF = null;
        trackRectF = null;
        bitmapPaint = null;
        trackPaint = null;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        bitmapPaint = null;
        super.setImageDrawable(drawable);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs != null) {
            parseAttrs(context, attrs);
        }

        onCreateAnimators();
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InsLoadingView);
        //int mStartColor = typedArray.getColor(R.styleable.InsLoadingView_start_color, startColor);
        //int mEndColor = typedArray.getColor(R.styleable.InsLoadingView_start_color, endColor);
        int mCircleDuration = typedArray.getInt(R.styleable.InsLoadingView_circle_duration, circleDuration);
        int mRotateDuration = typedArray.getInt(R.styleable.InsLoadingView_rotate_duration, rotateDuration);
        int status = typedArray.getInt(R.styleable.InsLoadingView_status, 0);

        if(mCircleDuration != circleDuration) {
            setCircleDuration(circleDuration);
        }

        if(mRotateDuration != rotateDuration) {
            setRotateDuration(rotateDuration);
        }

        setStartColor(startColor);
        setEndColor(endColor);
        setStatus(status);

        typedArray.recycle();
    }

    private void initPaints() {
        if(bitmapPaint == null) {
            bitmapPaint = getmBitmapPaint();
        }

        if(trackPaint == null) {
            trackPaint = getTrackPaint();
        }
    }

    private void initRectFs() {
        if(bitmapRectF == null) {
            float bitmapDia = circleDia - strokeWidth;
            bitmapRectF = new RectF(getWidth() * (1 - bitmapDia), getWidth() * (1 - bitmapDia), getWidth() * bitmapDia, getHeight() * bitmapDia);
        }

        if(trackRectF == null) {
            trackRectF = new RectF(getWidth() * (1 - circleDia), getWidth() * (1 - circleDia), getWidth() * circleDia, getHeight() * circleDia);
        }
    }

    private float centerX() {
        return getWidth() / 2.0f;
    }

    private float centerY() {
        return getHeight() / 2.0f;
    }

    private void onCreateAnimators() {
        rotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degrees = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(rotateDuration);
        rotateAnim.setRepeatCount(-1);

        circleAnim.setInterpolator(new DecelerateInterpolator());
        circleAnim.setDuration(circleDuration);
        circleAnim.setRepeatCount(-1);

        circleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(firstCircle) {
                    circleWidth = (float) animation.getAnimatedValue();
                } else {
                    circleWidth = (float) animation.getAnimatedValue() - 360;
                }

                postInvalidate();
            }
        });

        circleAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                firstCircle = !firstCircle;
            }
        });

        touchAnim.setInterpolator(new DecelerateInterpolator());
        touchAnim.setDuration(200);
        touchAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scale = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        startAnim();
    }

    private void drawBitmap(Canvas canvas) {
        canvas.drawOval(bitmapRectF, bitmapPaint);
    }

    private void drawTrack(Canvas canvas, Paint paint) {
        canvas.rotate(degrees, centerX(), centerY());
        canvas.rotate(ARC_WIDTH, centerX(), centerY());


        if(circleWidth < 0) {
            //a
            float startArg = circleWidth + 360;
            canvas.drawArc(trackRectF, startArg, 360 - startArg, false, paint);
            float adjustCricleWidth = circleWidth + 360;
            float width = 8;

            while (adjustCricleWidth > ARC_WIDTH) {
                width = width - arcChangeAngle;
                adjustCricleWidth = adjustCricleWidth - ARC_WIDTH;
                canvas.drawArc(trackRectF, adjustCricleWidth, width, false, paint);
            }
        } else {
            //b
            for(int i = 0; i <= 4; i++) {
                if(ARC_WIDTH * i > circleWidth) {
                    break;
                }

                canvas.drawArc(trackRectF, circleWidth - ARC_WIDTH * i, 8 + i, false, paint);
            }

            if(circleWidth > ARC_WIDTH * 4) {
                canvas.drawArc(trackRectF, 0, circleWidth - ARC_WIDTH * 4, false, paint);
            }

            float adjustCricleWidth = 360;
            float width = 8 * (360 - circleWidth) / 360;

            while (width > 0 && adjustCricleWidth > ARC_WIDTH) {
                width = width - arcChangeAngle;
                adjustCricleWidth = adjustCricleWidth - ARC_WIDTH;
                canvas.drawArc(trackRectF, adjustCricleWidth, width, false, paint);
            }
        }
    }

    private void drawCircle(Canvas canvas, Paint paint) {
        RectF rectF = new RectF(getWidth() * (1 - circleDia), getWidth() * (1 - circleDia), getWidth() * circleDia, getHeight() * circleDia);
        canvas.drawOval(rectF, paint);
    }

    private void drawClickedircle(Canvas canvas) {
        Paint paintClicked = new Paint();
        paintClicked.setColor(sClickedColor);
        setPaintStroke(paintClicked);
        drawCircle(canvas, paintClicked);
    }

    private void startDownAnim() {
        touchAnim.setFloatValues(scale, 0.9f);
        touchAnim.start();
    }

    private void startUpAnim() {
        touchAnim.setFloatValues(scale, 1);
        touchAnim.start();
    }

    private void startAnim() {
        rotateAnim.start();
        circleAnim.start();
    }

    private void endAnim() {
        if(rotateAnim != null) {
            rotateAnim.end();
        }

        if(circleAnim != null) {
            circleAnim.end();
        }
    }

    private Paint getTrackPaint() {
        Paint paint = new Paint();
        Shader shader = new LinearGradient(0f, 0f, (getWidth() * circleDia * (360 - ARC_WIDTH * 4) / 360),
                getHeight() * strokeWidth, startColor, endColor, CLAMP);
        paint.setShader(shader);
        setPaintStroke(paint);
        return paint;
    }

    private void setPaintStroke(Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getHeight() * strokeWidth);
    }

    private Paint getmBitmapPaint() {
        Paint paint = new Paint();
        Drawable drawable = getDrawable();
        Matrix matrix = new Matrix();

        if(null == drawable) {
            return paint;
        }

        Bitmap bitmap = drawableToBitmap(drawable);
        BitmapShader tshader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        int bSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        float scale = getWidth() * 1.0f / bSize;
        matrix.setScale(scale, scale);

        if(bitmap.getWidth() > bitmap.getHeight()) {
            matrix.postTranslate(-(bitmap.getWidth() * scale - getWidth()) / 2, 0);
        } else {
            matrix.postTranslate(0, -(bitmap.getHeight() * scale - getHeight()) / 2);
        }

        tshader.setLocalMatrix(matrix);
        paint.setShader(tshader);

        return paint;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        }

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }
}