package com.loong.common.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.recyclerview.widget.RecyclerView;

import com.loong.common.R;


public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Context context;
    private Paint mPaint;
    private float dividerHeight;
    private float paddingLeft = 0;
    private float paddingRight = 0;


    public DividerItemDecoration(Context context) {
        this.context = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint.setColor(context.getResources().getColor(R.color.transparent));

        dividerHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, context.getResources().getDisplayMetrics());
    }

    public DividerItemDecoration setDividerColor(@ColorRes int id) {
        mPaint.setColor(context.getResources().getColor(id));
        return this;
    }

    public DividerItemDecoration setDividerHeight(float dpValue) {
        dividerHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
        return this;
    }

    //region 设置Padding
    public DividerItemDecoration setPaddingLeft(float dpValue) {
        paddingLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
        return this;
    }

    public DividerItemDecoration setPaddingRight(float dpValue) {
        paddingRight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
        return this;
    }

    public DividerItemDecoration setPaddingHorizontal(float dpValue) {
        paddingRight = paddingLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
        return this;
    }
    //endregion


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = (int) dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        float left = parent.getPaddingLeft() + paddingLeft;
        float right = parent.getWidth() - (parent.getPaddingRight() + paddingRight);

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
