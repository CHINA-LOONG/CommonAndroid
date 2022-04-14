package com.loong.common.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loong.common.R;

import java.util.List;

public class SectionItemDecoration<T extends ISectionItem> extends RecyclerView.ItemDecoration {

    private List<T> mData;
    private Paint mBgPaint;
    private TextPaint mTextPaint;
    private Rect mBounds;

    private int mSectionHeight;
    private int mBgColor;
    private int mTextColor;
    private int mTextSize;

    public SectionItemDecoration(Context context, List<T> data) {
        this.mData = data;

        mBgColor = context.getResources().getColor(R.color.gray);

        mSectionHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36f, context.getResources().getDisplayMetrics());
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11f, context.getResources().getDisplayMetrics());

//        mSectionHeight = context.getResources().getDimensionPixelSize(R.dimen.dp36);
//        mTextSize = context.getResources().getDimensionPixelSize(R.dimen.f11);

//        mTextColor = context.getResources().getColor(R.color.cp_color_gray);
        mTextColor = 0xff7b7b7b;

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(mBgColor);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);

        mBounds = new Rect();
    }

    public void setData(List<T> data) {
        this.mData = data;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int position = params.getViewLayoutPosition();
            if (mData != null && !mData.isEmpty() && position <= mData.size() - 1 && position > -1) {
                if (position == 0) {
                    drawSection(c, left, right, child, params, position);
                } else {
                    if (null != mData.get(position).getSection() && !mData.get(position).getSection().equals(mData.get(position - 1).getSection())) {
                        drawSection(c, left, right, child, params, position);
                    }
                }
            }
        }
    }

    private void drawSection(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, int position) {
        c.drawRect(left, child.getTop() - params.topMargin - mSectionHeight, right, child.getTop() - params.topMargin, mBgPaint);
        mTextPaint.getTextBounds(mData.get(position).getSection(), 0, mData.get(position).getSection().length(), mBounds);
        c.drawText(mData.get(position).getSection(), child.getPaddingLeft(), child.getTop() - params.topMargin - (mSectionHeight / 2 - mBounds.height() / 2), mTextPaint);
    }


    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int pos = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        if (pos < 0) return;
        if (mData == null || mData.isEmpty()) return;
        String section = mData.get(pos).getSection();
        View child = parent.findViewHolderForLayoutPosition(pos).itemView;

        boolean flag = false;
        if ((pos + 1) < mData.size()) {
            if (null != section && !section.equals(mData.get(pos + 1).getSection())) {
                if (child.getHeight() + child.getTop() < mSectionHeight) {
                    c.save();
                    flag = true;
                    c.translate(0, child.getHeight() + child.getTop() - mSectionHeight);
                }
            }
        }
        c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + mSectionHeight, mBgPaint);
        mTextPaint.getTextBounds(section, 0, section.length(), mBounds);
        c.drawText(section, child.getPaddingLeft(), parent.getPaddingTop() + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2), mTextPaint);
        if (flag)
            c.restore();

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (mData != null && !mData.isEmpty() && position <= mData.size() - 1 && position > -1) {
            if (position == 0) {
                outRect.set(0, mSectionHeight, 0, 0);
            } else {
                if (null != mData.get(position).getSection()
                        && !mData.get(position).getSection().equals(mData.get(position - 1).getSection())) {
                    outRect.set(0, mSectionHeight, 0, 0);
                }
            }
        }
    }
}
