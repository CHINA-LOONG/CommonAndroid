package com.loong.common.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * author : xuelong
 * e-mail : xuelong9009@qq.com
 * date   : 2021/7/125:53 下午
 * desc   :
 * version: 1.0
 */
public class GridLayoutDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private int spanCount = 3;

    private int top = 0;
    private int space = 10;

    public GridLayoutDecoration(){ }
    public GridLayoutDecoration(Context context, int spanCount){
        this.mContext = context;
        this.spanCount = spanCount;
    }
    public GridLayoutDecoration(Context context, int spanCount,float dpSpace){
        this.spanCount = spanCount;
        space = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSpace, context.getResources().getDisplayMetrics());
    }
    public GridLayoutDecoration(Context context, int spanCount,float dpTop,float dpSpace){
        this.spanCount = spanCount;
        top =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpTop, context.getResources().getDisplayMetrics());
        space = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSpace, context.getResources().getDisplayMetrics());
    }

    public GridLayoutDecoration setTop(Context context,float dpValue){
        top =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
        return this;
    }
    public GridLayoutDecoration setSpace(Context context, float dpValue){
        space =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
        return this;
    }


    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view)<spanCount){
            outRect.top = top;
        }

//        if (parent.getChildLayoutPosition(view)%spanCount == 0){
//            outRect.left = 0;
//        }else {
////            outRect.left = space;
//        }
        outRect.bottom =space;
    }
}
