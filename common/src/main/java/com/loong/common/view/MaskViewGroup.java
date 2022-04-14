package com.loong.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.loong.common.R;

/**
 * author : xuelong
 * e-mail : xuelong9009@qq.com
 * date   : 2020/11/144:27 PM
 * desc   :
 * version: 1.0
 */
public class MaskViewGroup extends ViewGroup {


    //阴影相关
    private Paint mShadowPaint; // 阴影画笔
    private boolean drawShadow; // 是否绘制阴影
    private int shadowColor=Color.parseColor("#333333"); // 阴影的颜色，设置给阴影的画笔
    private float shadowRadius; // 阴影的模糊半径，越大越模糊
    private float dx; // 阴影的偏移
    private float dy;
    private float cornerRadius; //阴影的圆角


    // 绘制一个和阴影副本相同的矩形，用于覆盖掉阴影副本
    private int showColor=Color.WHITE; // 用于覆盖阴影的颜色
    private Paint mFillPaint;


    //因为绘制阴影所造成的布局边距
    private float deltaLength;
    private float deltaLengthLeft;
    private float deltaLengthRight;
    private float deltaLengthTop;
    private float deltaLengthBottom;


    public MaskViewGroup(Context context) {
        this(context, null);
    }

    public MaskViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    /**
     * 初始化信息变量
     */
    private void initView(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MaskViewGroup);

        drawShadow = a.getBoolean(R.styleable.MaskViewGroup_enable,true);
        shadowColor = a.getColor(R.styleable.MaskViewGroup_containerShadowColor, Color.parseColor("#333333"));
        shadowRadius = a.getDimension(R.styleable.MaskViewGroup_containerShadowRadius, 0);
        dx = a.getDimension(R.styleable.MaskViewGroup_deltaX, 0);
        dy = a.getDimension(R.styleable.MaskViewGroup_deltaY, 0);
        cornerRadius = a.getDimension(R.styleable.MaskViewGroup_containerCornerRadius, 0);

        // 内容区域
        showColor=a.getColor(R.styleable.MaskViewGroup_containerShowColor,Color.WHITE);

        //边距属性
        deltaLength = a.getDimension(R.styleable.MaskViewGroup_containerDeltaLength, 0);
        deltaLengthLeft = a.getDimension(R.styleable.MaskViewGroup_containerDeltaLengthLeft, 0);
        deltaLengthRight = a.getDimension(R.styleable.MaskViewGroup_containerDeltaLengthRight, 0);
        deltaLengthTop = a.getDimension(R.styleable.MaskViewGroup_containerDeltaLengthTop, 0);
        deltaLengthBottom = a.getDimension(R.styleable.MaskViewGroup_containerDeltaLengthBottom, 0);
        if (deltaLength > 0) {
            deltaLengthLeft = deltaLength;
            deltaLengthRight = deltaLength;
            deltaLengthTop = deltaLength;
            deltaLengthBottom = deltaLength;
        }


        a.recycle();
        initShadowPaint();
    }


    //初始化阴影画笔、填充画笔
    private void initShadowPaint() {
        mShadowPaint = new Paint();
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setColor(shadowColor);
        mShadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);


        mFillPaint = new Paint();
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(showColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() != 1) {
            throw new IllegalStateException("子View只能有一个");
        }

        int measureWidth = getMeasuredWidth();
        int measureHeight = getMeasuredHeight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //获取唯一子控件，为了更好控制某一边显示阴影， 不再计算子控件的Margin属性，用我们的边距属性代替
        View child = getChildAt(0);
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

        int childBottomMargin = (int) deltaLengthBottom;
        int childLeftMargin = (int) deltaLengthLeft;
        int childRightMargin = (int) deltaLengthRight;
        int childTopMargin = (int) deltaLengthTop;


        // 为唯一子控件 计算测量模式
        int widthMeasureSpecMode;
        int widthMeasureSpecSize;
        int heightMeasureSpecMode;
        int heightMeasureSpecSize;

        if (widthMode == MeasureSpec.UNSPECIFIED){
            widthMeasureSpecMode = MeasureSpec.UNSPECIFIED;
            widthMeasureSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        }else {
            if (layoutParams.width == LayoutParams.MATCH_PARENT){
                widthMeasureSpecMode = MeasureSpec.EXACTLY;
                widthMeasureSpecSize = measureWidth - childLeftMargin - childRightMargin;
            } else if (LayoutParams.WRAP_CONTENT == layoutParams.width) {
                widthMeasureSpecMode = MeasureSpec.AT_MOST;
                widthMeasureSpecSize = measureWidth - childLeftMargin - childRightMargin;
            } else {
                widthMeasureSpecMode = MeasureSpec.EXACTLY;
                widthMeasureSpecSize = layoutParams.width;
            }
        }
        if (heightMode == MeasureSpec.UNSPECIFIED){
            heightMeasureSpecMode = MeasureSpec.UNSPECIFIED;
            heightMeasureSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        }else {
            if (layoutParams.height == LayoutParams.MATCH_PARENT) {
                heightMeasureSpecMode = MeasureSpec.EXACTLY;
                heightMeasureSpecSize = measureHeight - childBottomMargin - childTopMargin;
            } else if (LayoutParams.WRAP_CONTENT == layoutParams.height) {
                heightMeasureSpecMode = MeasureSpec.AT_MOST;
                heightMeasureSpecSize = measureHeight - childBottomMargin - childTopMargin;
            } else {
                heightMeasureSpecMode = MeasureSpec.EXACTLY;
                heightMeasureSpecSize = layoutParams.height;
            }
        }
        measureChild(child,MeasureSpec.makeMeasureSpec(widthMeasureSpecSize,widthMeasureSpecMode),MeasureSpec.makeMeasureSpec(heightMeasureSpecSize,heightMeasureSpecMode));

        int parentWidthMeasureSpec = MeasureSpec.getMode(widthMeasureSpec);
        int parentHeightMeasureSpec = MeasureSpec.getMode(heightMeasureSpec);
        int height = measureHeight;
        int width = measureWidth;

        //获取唯一子控件的测量结果
        int childHeight = child.getMeasuredHeight();
        int childWidth = child.getMeasuredWidth();
        if (parentWidthMeasureSpec == MeasureSpec.AT_MOST){
            width = childWidth + childRightMargin + childLeftMargin;
        }
        if (parentHeightMeasureSpec == MeasureSpec.AT_MOST){
            height = childHeight + childTopMargin + childBottomMargin;
        }

        //如果没有加边距，则需要再加上边距

        if (width < childWidth + deltaLengthLeft + deltaLengthRight) {
            width = (int) (childWidth + deltaLengthLeft + deltaLengthRight);
        }
        if (height < childHeight + deltaLengthTop + deltaLengthBottom) {
            height = (int) (childHeight + deltaLengthTop + deltaLengthBottom);
        }

        if (height != measureHeight || width != measureWidth) {
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        View child = getChildAt(0);

        int childMeasureWidth = child.getMeasuredWidth();
        int childMeasureHeight = child.getMeasuredHeight();
        child.layout((int) deltaLengthLeft,(int) deltaLengthTop,childMeasureWidth+(int) deltaLengthLeft,childMeasureHeight+(int) deltaLengthTop);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        if (drawShadow) {
            //对单独的View在运行时阶段禁用硬件加速
            //setShadowLayer只有文字绘制阴影支持硬件加速，其它都不支持硬件加速，所以为了方便起见，我们需要在自定义控件中禁用硬件加速
            if (getLayerType() != LAYER_TYPE_SOFTWARE) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
            View child = getChildAt(0);
            int left = child.getLeft();
            int top = child.getTop();
            int right = child.getRight();
            int bottom = child.getBottom();
            RectF rectF = new RectF(left, top, right, bottom);
            if (cornerRadius==0f){
                canvas.drawRect(rectF,mShadowPaint);
                canvas.drawRect(rectF,mFillPaint);
            }else {
                canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, mShadowPaint);
                canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, mFillPaint);
            }
        }


        super.dispatchDraw(canvas);
    }
}
