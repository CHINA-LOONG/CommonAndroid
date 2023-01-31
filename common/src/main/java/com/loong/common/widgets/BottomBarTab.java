package com.loong.common.widgets;

/*******************************************************************
 * BottomBarTab.java  2019/4/12
 * <P>
 * <br/>
 * <br/>
 * </p>
 * Copyright2019 by CNPC Company. All Rights Reserved.
 *
 * @author:chengzm
 *
 ******************************************************************/

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.loong.common.R;


/**
 * Created by YoKeyword on 16/6/3.
 */
public class BottomBarTab extends FrameLayout {
    private ImageView mIcon;
    private TextView mTvTitle;
    private Context mContext;
    private int mTabPosition = -1;

    private String titleColor = "#111111";

    private @DrawableRes
    int selectIcon;
    private @DrawableRes
    int normalIcon;

    private TextView mTvUnreadCount;

    private AnimationSet animationSet;
    private boolean isAnimationPlaying = false;

    public BottomBarTab(Context context, @DrawableRes int[] icon, CharSequence title) {
        this(context, null, icon, title, "#111111");
    }

    public BottomBarTab(Context context, @DrawableRes int[] icon, CharSequence title, String normalColor) {
        this(context, null, icon, title, normalColor);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int[] icon, CharSequence title, String normalColor) {
        this(context, attrs, 0, icon, title, normalColor);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int defStyleAttr, int[] icon, CharSequence title, String normalColor) {
        super(context, attrs, defStyleAttr);
        this.titleColor = normalColor;
        init(context, icon, title);
    }

    private void init(Context context, int[] icon, CharSequence title) {

        if (icon.length < 2) {
            Log.e("BottomBarTab", "图片个数小于2");
            return;
        }

        normalIcon = icon[0];
        selectIcon = icon[1];

        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{com.lxj.xpopup.R.attr.selectableItemBackgroundBorderless});
        Drawable drawable = typedArray.getDrawable(0);
        setBackgroundDrawable(drawable);
        typedArray.recycle();

        LinearLayout lLContainer = new LinearLayout(context);
        lLContainer.setOrientation(LinearLayout.VERTICAL);
        lLContainer.setGravity(Gravity.CENTER);
        LayoutParams paramsContainer = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        paramsContainer.topMargin = 16;
        paramsContainer.gravity = Gravity.CENTER;
        lLContainer.setLayoutParams(paramsContainer);

        mIcon = new ImageView(context);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 23, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        mIcon.setImageResource(normalIcon);
        mIcon.setColorFilter(Color.parseColor(titleColor));
        mIcon.setLayoutParams(params);
        lLContainer.addView(mIcon);

        mTvTitle = new TextView(context);
        mTvTitle.setText(title);
        LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsTv.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        mTvTitle.setTextSize(11);
        mTvTitle.setTextColor(Color.parseColor(titleColor));
        mTvTitle.setLayoutParams(paramsTv);
        lLContainer.addView(mTvTitle);

        addView(lLContainer);

//        int min = dip2px(context, 20);
//        int padding = dip2px(context, 5);
//        mTvUnreadCount = new TextView(context);
//        mTvUnreadCount.setBackgroundResource(R.drawable.bg_msg_bubble);
//        mTvUnreadCount.setMinWidth(min);
//        mTvUnreadCount.setTextColor(Color.WHITE);
//        mTvUnreadCount.setPadding(padding, 0, padding, 0);
//        mTvUnreadCount.setGravity(Gravity.CENTER);
//        FrameLayout.LayoutParams tvUnReadParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, min);
//        tvUnReadParams.gravity = Gravity.CENTER;
//        tvUnReadParams.leftMargin = dip2px(context, 17);
//        tvUnReadParams.bottomMargin = dip2px(context, 14);
//        mTvUnreadCount.setLayoutParams(tvUnReadParams);
//        mTvUnreadCount.setVisibility(GONE);

//        addView(mTvUnreadCount);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            mIcon.setImageResource(selectIcon);
            mIcon.setColorFilter(getResources().getColor(R.color.colorPrimary));
            mTvTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            mIcon.setImageResource(normalIcon);
            mIcon.setColorFilter(Color.parseColor(titleColor));
            mTvTitle.setTextColor(Color.parseColor(titleColor));
        }
    }

    public void setTabPosition(int position) {
        mTabPosition = position;
        if (position == 0) {
            setSelected(true);
        }
    }

    public int getTabPosition() {
        return mTabPosition;
    }

    /**
     * 设置未读数量
     */
    public void setUnreadCount(int num) {
        if (num <= 0) {
            mTvUnreadCount.setText(String.valueOf(0));
            mTvUnreadCount.setVisibility(GONE);
        } else {
            mTvUnreadCount.setVisibility(VISIBLE);
            if (num > 99) {
                mTvUnreadCount.setText("99+");
            } else {
                mTvUnreadCount.setText(String.valueOf(num));
            }
        }
    }

    /**
     * 获取当前未读数量
     */
    public int getUnreadCount() {
        int count = 0;
        if (TextUtils.isEmpty(mTvUnreadCount.getText())) {
            return count;
        }
        if (mTvUnreadCount.getText().toString().equals("99+")) {
            return 99;
        }
        try {
            count = Integer.valueOf(mTvUnreadCount.getText().toString());
        } catch (Exception ignored) {
        }
        return count;
    }

    private int dip2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public void playScaleAnimation() {

        if (animationSet == null) {
            animationSet = new AnimationSet(true); //true表示共用同一个插值器

            //缩小
            ScaleAnimation scaleAnimation_small = new ScaleAnimation(1f, 0.9f, 1f, 0.9f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation_small.setInterpolator(new LinearInterpolator());
            scaleAnimation_small.setDuration(150);

            ScaleAnimation scaleAnimation_big = new ScaleAnimation(0.9f, 1f, 0.9f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation_big.setInterpolator(new LinearInterpolator());
            scaleAnimation_big.setDuration(150);

            animationSet.addAnimation(scaleAnimation_small);
            animationSet.addAnimation(scaleAnimation_big);

            //动画完成后保持位置
            animationSet.setFillAfter(false);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isAnimationPlaying = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnimationPlaying = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        if (!isAnimationPlaying) {

            //开始动画
            this.startAnimation(animationSet);
        }


    }
}
