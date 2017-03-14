package com.transsion.touchevent;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by xuecci on 2016/12/24.
 */

public class TorchView extends View {

    private final static String TAG = TorchView.class.getSimpleName();

    private Context mContext;
    private int mHeight;
    private int mWidth;

    private int mIconWidth;
    private int mIconHeight;

    private Drawable mIcon;
    private int mBordersize = 88;
    private int mRadius = 200;
    private Paint mPaint;
    private int mColor[] = {0xffc17b41,0xccc17b41,0x99c17b41,0x66c17b41,0x33c17b41};
    private boolean mIsTorchOn = false;

    public float getFactor0() {
        return factor0;
    }

    public void setFactor0(float factor0) {
        this.factor0 = factor0;
    }

    public float getFactor1() {
        return factor1;
    }

    public void setFactor1(float factor1) {
        this.factor1 = factor1;
    }

    public float getFactor2() {
        return factor2;
    }

    public void setFactor2(float factor2) {
        this.factor2 = factor2;
    }

    public float getFactor3() {
        return factor3;
    }

    public void setFactor3(float factor3) {
        this.factor3 = factor3;
    }

    public float getFactor4() {
        return factor4;
    }

    public void setFactor4(float factor4) {
        this.factor4 = factor4;
    }

    private float factor0 = 1.0f;
    private float factor1 = 1.0f;
    private float factor2 = 1.0f;
    private float factor3 = 1.0f;
    private float factor4 = 1.0f;

    private AnimatorSet mAnimator;
    private AnimatorSet mAnimatorReverse;
    private ObjectAnimator mObjectAnimator[] = new ObjectAnimator[5];
    private ObjectAnimator mObjectAnimatorReverse[] = new ObjectAnimator[5];
    private Path mPath;

    public TorchView(Context context) {
        this(context,null);
    }

    public TorchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TorchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        init();
        initAnimator();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mIsTorchOn) {
                    mAnimator.start();
                    mIsTorchOn = true;
                } else {
                    mAnimatorReverse.start();
                    mIsTorchOn = false;
                }
            }
        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHeight = h;
        mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawIcon(canvas);
        canvas.save();
        canvas.clipRect(0,0,mWidth,mHeight);
        mPath.reset();
        mPath.addCircle(mWidth,mHeight/2,mRadius*factor0,Path.Direction.CCW);
        canvas.clipPath(mPath, Region.Op.DIFFERENCE);
        drawGlow(canvas);
    }

    private void drawGlow(Canvas canvas) {
        float mFactor = 1.0f;
        for(int i=0;i<5;i++) {
            mPaint.setColor(mColor[i]);
            switch (i) {
                case 0:
                    mFactor = factor0;
                    break;
                case 1:
                    mFactor = factor1;
                    break;
                case 2:
                    mFactor = factor2;
                    break;
                case 3:
                    mFactor = factor3;
                    break;
                case 4:
                    mFactor = factor4;
                    break;
            }
            canvas.drawCircle(mWidth,mHeight/2,(mRadius+mBordersize*(i+1))*mFactor,mPaint);
            canvas.restore();

            canvas.save();
            canvas.clipRect(0,0,mWidth,mHeight);
            mPath.reset();
            mPath.addCircle(mWidth,mHeight/2,(mRadius+mBordersize*(i+1))*mFactor,Path.Direction.CCW);
            canvas.clipPath(mPath, Region.Op.DIFFERENCE);
        }
        canvas.restore();
    }

    private void drawIcon(Canvas canvas) {
        mIcon.setBounds((int)(mWidth-mIconWidth*factor0),(int) (mHeight/2-mIconHeight*factor0/2),mWidth,(int)(mHeight/2+mIconHeight*factor0/2));
        canvas.save();
        mPath.reset();
        canvas.clipPath(mPath);
        mPath.addCircle(mWidth,mHeight/2,mRadius*factor0,Path.Direction.CCW);
        canvas.clipPath(mPath,Region.Op.REPLACE);
        mIcon.draw(canvas);
        canvas.restore();
    }

    private void init() {
        mIcon = ActivityCompat.getDrawable(mContext,R.mipmap.icon);
        mIconWidth = mIcon.getIntrinsicWidth();
        mIconHeight = mIcon.getIntrinsicHeight();
        mRadius = mIconWidth;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mBordersize);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);

        mPath = new Path();
    }

    private void initAnimator() {

        mObjectAnimator[0] = ObjectAnimator.ofFloat(TorchView.this,"factor0",1.0f,1.2f).setDuration(300);
        mObjectAnimator[0].setStartDelay(0);
        mObjectAnimator[0].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });

        mObjectAnimator[1] = ObjectAnimator.ofFloat(TorchView.this,"factor1",1.0f,1.2f).setDuration(300);
        mObjectAnimator[1].setStartDelay(30);
        mObjectAnimator[2] = ObjectAnimator.ofFloat(TorchView.this,"factor2",1.0f,1.2f).setDuration(300);
        mObjectAnimator[2].setStartDelay(60);
        mObjectAnimator[3] = ObjectAnimator.ofFloat(TorchView.this,"factor3",1.0f,1.2f).setDuration(300);
        mObjectAnimator[3].setStartDelay(90);
        mObjectAnimator[4] = ObjectAnimator.ofFloat(TorchView.this,"factor4",1.0f,1.2f).setDuration(300);
        mObjectAnimator[4].setStartDelay(120);
        mObjectAnimator[4].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });

        mObjectAnimatorReverse[0] = ObjectAnimator.ofFloat(TorchView.this,"factor0",1.2f,1.0f).setDuration(300);
        mObjectAnimatorReverse[0].setStartDelay(0);
        mObjectAnimatorReverse[0].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });

        mObjectAnimatorReverse[1] = ObjectAnimator.ofFloat(TorchView.this,"factor1",1.2f,1.0f).setDuration(300);
        mObjectAnimatorReverse[1].setStartDelay(30);
        mObjectAnimatorReverse[2] = ObjectAnimator.ofFloat(TorchView.this,"factor2",1.2f,1.0f).setDuration(300);
        mObjectAnimatorReverse[2].setStartDelay(60);
        mObjectAnimatorReverse[3] = ObjectAnimator.ofFloat(TorchView.this,"factor3",1.2f,1.0f).setDuration(300);
        mObjectAnimatorReverse[3].setStartDelay(90);
        mObjectAnimatorReverse[4] = ObjectAnimator.ofFloat(TorchView.this,"factor4",1.2f,1.0f).setDuration(300);
        mObjectAnimatorReverse[4].setStartDelay(120);
        mObjectAnimatorReverse[4].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });

        mAnimator = new AnimatorSet();
        mAnimator.setInterpolator(new OvershootInterpolator());
        mAnimator.playTogether(mObjectAnimator[0],mObjectAnimator[1],mObjectAnimator[2],mObjectAnimator[3],mObjectAnimator[4]);


        mAnimatorReverse = new AnimatorSet();
        mAnimatorReverse.setInterpolator(new OvershootInterpolator());
        mAnimatorReverse.playTogether(mObjectAnimatorReverse[0],mObjectAnimatorReverse[1],mObjectAnimatorReverse[2],mObjectAnimatorReverse[3],mObjectAnimatorReverse[4]);
    }
}
