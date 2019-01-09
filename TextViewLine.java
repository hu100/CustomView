package com.gosuncn.gorobot.module.laser.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.gosuncn.gorobot.R;

/**
 * Created by Administrator on 2018/3/23 0023.
 * 功能：1.顶部或者底部分割线
 * 2.每行文字下面的分割线
 * 3.分割线类型可以是color或者drawable，在xml中使用一个就行
 * <p>
 * <declare-styleable name="TextViewLine">
 * <attr name="dividerColor" format="color"/>
   <attr name="dividerRowColor" format="color"/>
   <attr name="dividerDrawable" format="reference"/>
   <attr name="dividerHeight" format="dimension"/>
   <attr name="dividerTop" format="boolean"/>
   <attr name="dividerBottom" format="boolean"/>
   <attr name="dividerRow" format="boolean"/>
 * </declare-styleable>
 */

public class TextViewLine extends TextView {

    private int mDividerColor;//分割线的颜色
    private int mDividerRowColor;//每行下划线的颜色
    private float mLineHeight;//分割线高度
    private Drawable mLineDrawable;//分割线drawable
    private boolean mDrawTopLine;//是否有顶部分割线，默认false
    private boolean mDrawBottomLine;//是否有底部分割线，默认true
    private boolean mDrawLowLine;//每行是否要分割线，默认false
    private Paint mPaint;
    private Rect mRect;

    public TextViewLine(Context context) {
        this(context, null);
    }

    public TextViewLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewLine, defStyleAttr, 0);
        mDividerColor = typedArray.getColor(R.styleable.TextViewLine_dividerColor, Color.BLACK);
        mDividerRowColor = typedArray.getColor(R.styleable.TextViewLine_dividerRowColor, Color.RED);
        mLineHeight = typedArray.getDimension(R.styleable.TextViewLine_dividerHeight, 2);
        mLineDrawable = typedArray.getDrawable(R.styleable.TextViewLine_dividerDrawable);
        mDrawTopLine = typedArray.getBoolean(R.styleable.TextViewLine_dividerTop, false);
        mDrawBottomLine = typedArray.getBoolean(R.styleable.TextViewLine_dividerBottom, true);
        mDrawLowLine = typedArray.getBoolean(R.styleable.TextViewLine_dividerRow, false);
        typedArray.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(mLineHeight);
        mPaint.setColor(mDividerColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height;
        if (mDrawBottomLine || mDrawTopLine) {
            height = (int) (getMeasuredHeight() + mLineHeight);
            setMeasuredDimension(getMeasuredWidth(), height);
        }else if (mDrawBottomLine && mDrawTopLine) {
            height = (int) (getMeasuredHeight() + mLineHeight * 2);
            setMeasuredDimension(getMeasuredWidth(), height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画每行分割线
        drawRowLine(canvas);
        //画顶部和底部分割线
        drawDivider(canvas);
    }

    private void drawRowLine(Canvas canvas) {
        int count = getLineCount();
        for (int i = 0; i < count; i++) {
            if (i == count - 1) {
                //最后一行不画分割线,如果要画，不需要 减getLineSpacingExtra()
                 break;
            }
            if (mDrawLowLine) {
                getLineBounds(i, mRect);
                mPaint.setColor(mDividerRowColor);
                canvas.drawRect(mRect.left, (int) (mRect.bottom - getLineSpacingExtra()),
                        mRect.right, (int) (mRect.bottom - getLineSpacingExtra() + mLineHeight),mPaint);
                }
                if (mLineDrawable != null) {
                    mLineDrawable.setBounds(mRect.left, (int) (mRect.bottom - getLineSpacingExtra()),
                            mRect.right, (int) (mRect.bottom - getLineSpacingExtra() + mLineHeight));
                    mLineDrawable.draw(canvas);

                }
        }
    }

    private void drawDivider(Canvas canvas) {
        if (mDrawTopLine) {
            //画顶部线
            mPaint.setColor(mDividerColor);
            canvas.drawRect(0, 0, getWidth(), mLineHeight, mPaint);
            if (mLineDrawable != null) {
                mRect.left = 0;
                mRect.top = 0;
                mRect.right = getWidth();
                mRect.bottom = (int) mLineHeight;
                mLineDrawable.setBounds(mRect);
                mLineDrawable.draw(canvas);
            }
        }

        if (mDrawBottomLine) {
            mPaint.setColor(mDividerColor);
            canvas.drawRect(0, getHeight() - mLineHeight, getWidth(), getHeight(), mPaint);
            if (mLineDrawable != null) {
                mRect.left = 0;
                mRect.top = (int) (getHeight() - mLineHeight);
                mRect.right = getWidth();
                mRect.bottom = getHeight();
                mLineDrawable.setBounds(mRect);
                mLineDrawable.draw(canvas);
            }
        }
    }

}
