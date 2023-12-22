package com.zam.sidik_sumenep.util.customclasses;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public class ExpandableLayout extends LinearLayout implements View.OnClickListener {
    private int mWidthMeasureSpec;
    private int mHeightMeasureSpec;
    private boolean mAttachedToWindow;
    private boolean mFirstLayout = true;
    private boolean mInLayout;
    private ObjectAnimator mExpandAnimator;
    private OnExpandListener mListener;
    private Context context;
    private int resImgView;
    private ImageView parentImage;

    public ExpandableLayout(Context context) {
        super(context);
        this.init(context);
    }

    public ExpandableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public ExpandableLayout(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    public ExpandableLayout(Context context, AttributeSet attrs,
                            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context);
    }

    private void init(Context c) {
        context = c;
        this.setOrientation(LinearLayout.VERTICAL);
    }

    @SuppressLint("DiscouragedApi")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidthMeasureSpec = widthMeasureSpec;
        mHeightMeasureSpec = heightMeasureSpec;
        //View child = findExpandableView();
        View child = findChild();
        if (child != null) {
            LayoutParams p = (LayoutParams) child.getLayoutParams();

            if (p.weight != 0) {
                throw new IllegalArgumentException(
                        "ExpandableView can't use weight");
            }

            if (!p.isExpanded && !p.isExpanding) {
                child.setVisibility(View.GONE);
            } else {
                child.setVisibility(View.VISIBLE);
            }
        }
        findHeader().setOnClickListener(this);
        resImgView = context.getResources().getIdentifier("img_header_expandable", "id", context.getPackageName());
        parentImage = (ImageView) findHeader().findViewById(resImgView);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mInLayout = true;
        super.onLayout(changed, l, t, r, b);
        mInLayout = false;
        mFirstLayout = false;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttachedToWindow = false;
        //View child = findExpandableView();
        View child = findChild();
        if (mExpandAnimator != null && mExpandAnimator.isRunning()) {
            mExpandAnimator.end();
            mExpandAnimator = null;
        }
        if (child != null) {
            LayoutParams p = (LayoutParams) child.getLayoutParams();
            if (p.isExpanded) {
                p.height = p.originalHeight;
                child.setVisibility(View.VISIBLE);
            } else {
                p.height = p.originalHeight;
                child.setVisibility(View.GONE);
            }
            p.isExpanding = false;
        }
    }

    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    public View findHeader() {
        return this.getChildAt(0);
    }

    public View findChild() {
        return this.getChildAt(1);
    }

    /*
        public View findExpandableView() {
            for (int i = 0; i < this.getChildCount(); i++) {
                LayoutParams p = (LayoutParams) this.getChildAt(i).getLayoutParams();
                if (p.canExpand) {
                    return this.getChildAt(i);
                }
            }
            return null;
        }
    */
    boolean checkExpandableView(View expandableView) {
        //LayoutParams p = (LayoutParams) expandableView.getLayoutParams();
        return true;
    }

    public boolean isExpanded() {
        //View child = findExpandableView();
        View child = findChild();
        if (child != null) {
            LayoutParams p = (LayoutParams) child.getLayoutParams();
            return p.isExpanded;
        }
        return false;
    }

    /**
     * @return
     */
    public boolean toggleExpansion() {
        return this.setExpanded(!isExpanded(), true);
    }

    /**
     * @param isExpanded
     * @return
     */
    public boolean setExpanded(boolean isExpanded) {
        return this.setExpanded(isExpanded, false);
    }

    /**
     * @param isExpanded
     * @param shouldAnimate
     * @return
     */
    public boolean setExpanded(boolean isExpanded, boolean shouldAnimate) {
        boolean result = false;
        //View child = findExpandableView();
        View child = findChild();
        if (child != null) {
            if (isExpanded != this.isExpanded()) {
                if (isExpanded) {
                    result = this.expand(child, shouldAnimate);
                } else {
                    result = this.collapse(child, shouldAnimate);
                }
            }
        }
        this.requestLayout();
        return result;
    }

    public void setOnExpandListener(OnExpandListener listenr) {
        this.mListener = listenr;
    }

    /**
     * @param child
     * @param shouldAnimate
     * @return
     */
    private boolean expand(View child, boolean shouldAnimate) {
        boolean result = false;
        if (!checkExpandableView(child)) {
            throw new IllegalArgumentException(
                    "expand(), View is not expandableView");
        }
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (mFirstLayout || !mAttachedToWindow || !shouldAnimate) {
            p.isExpanded = true;
            p.isExpanding = false;
            p.height = p.originalHeight;
            child.setVisibility(View.VISIBLE);
            result = true;
        } else {
            if (!p.isExpanded && !p.isExpanding) {
                this.playExpandAnimation(child);
                result = true;
            }
        }
        return result;
    }

    private void playExpandAnimation(final View child) {
        final LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p.isExpanding) {
            return;
        }
        child.setVisibility(View.VISIBLE);
        p.isExpanding = true;
        this.measure(mWidthMeasureSpec, mHeightMeasureSpec);
        final int measuredHeight = child.getMeasuredHeight();
        p.height = 0;

        mExpandAnimator = ObjectAnimator.ofInt(p, "height", 0, measuredHeight);
        mExpandAnimator.setDuration(getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime));
        mExpandAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                dispatchOffset(child);
                child.requestLayout();
            }
        });
        mExpandAnimator.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                performToggleState(child);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }
        });
        mExpandAnimator.start();
        startRotasiExp(findHeader());
    }

    /**
     * @param child
     * @param shouldAnimation
     * @return
     */
    private boolean collapse(View child, boolean shouldAnimation) {
        boolean result = false;
        if (!checkExpandableView(child)) {
            throw new IllegalArgumentException(
                    "collapse(), View is not expandableView");
        }
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (mFirstLayout || !mAttachedToWindow || !shouldAnimation) {
            p.isExpanded = false;
            p.isExpanding = false;
            p.height = p.originalHeight;
            child.setVisibility(View.GONE);
            result = true;
        } else {
            if (p.isExpanded && !p.isExpanding) {
                this.playCollapseAnimation(child);
                result = true;
            }
        }
        return result;
    }

    private void playCollapseAnimation(final View child) {
        final LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p.isExpanding) {
            return;
        }
        child.setVisibility(View.VISIBLE);
        p.isExpanding = true;
        this.measure(mWidthMeasureSpec, mHeightMeasureSpec);
        final int measuredHeight = child.getMeasuredHeight();

        mExpandAnimator = ObjectAnimator.ofInt(p, "height", measuredHeight, 0);
        mExpandAnimator.setDuration(getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime));
        mExpandAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                dispatchOffset(child);
                child.requestLayout();
            }
        });
        mExpandAnimator.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                performToggleState(child);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        mExpandAnimator.start();
        startRotasiColl(findHeader());
    }

    public boolean isRunningAnimation() {
        //View child = findExpandableView();
        View child = findChild();
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        return p.isExpanding == true;
    }

    private void dispatchOffset(View child) {
        if (mListener != null) {
            mListener.onExpandOffset(this, child, child.getHeight(),
                    !isExpanded());
        }
    }

    private void performToggleState(View child) {
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p.isExpanded) {
            p.isExpanded = false;
            if (mListener != null) {
                mListener.onToggle(this, child, false);
            }
            child.setVisibility(View.GONE);
            p.height = p.originalHeight;
        } else {
            p.isExpanded = true;
            if (mListener != null) {
                mListener.onToggle(this, child, true);
            }
        }
        p.isExpanding = false;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        if (isExpanded()) {
            ss.isExpanded = true;
        }
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.isExpanded) {
            //View child = findExpandableView();
            View child = findChild();
            if (child != null) {
                setExpanded(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        this.toggleExpansion();
    }

    public void startRotasiExp(View v) {
        int resAnim = context.getResources().getIdentifier("rotasi_expand", "anim", context.getPackageName());
        int resIcon = context.getResources().getIdentifier("ic_expand_less", "drawable", context.getPackageName());
        Animation starRotasi = AnimationUtils.loadAnimation(context, resAnim);
        parentImage.startAnimation(starRotasi);
        parentImage.setImageResource(resIcon);
    }

    public void startRotasiColl(View v) {
        int resAnim = context.getResources().getIdentifier("rotasi_expand", "anim", context.getPackageName());
        int resIcon = context.getResources().getIdentifier("ic_expand_more", "drawable", context.getPackageName());
        Animation starRotasi = AnimationUtils.loadAnimation(context, resAnim);
        parentImage.startAnimation(starRotasi);
        parentImage.setImageResource(resIcon);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(this.getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(
            android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p) && (p instanceof LayoutParams);
    }

    public static interface OnExpandListener {
        public void onToggle(ExpandableLayout view, View child, boolean isExpanded);

        public void onExpandOffset(ExpandableLayout view, View child,
                                   float offset, boolean isExpanding);
    }

    private static class SavedState extends BaseSavedState {

        @SuppressWarnings("unused")
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        boolean isExpanded;

        public SavedState(Parcel source) {
            super(source);
            isExpanded = source.readInt() == 1;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(isExpanded ? 1 : 0);
        }
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {
        private static final int NO_MESURED_HEIGHT = -10;
        int originalHeight = NO_MESURED_HEIGHT;
        boolean isExpanded;
        //boolean canExpand;
        boolean isExpanding;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            //canExpand = true;
            isExpanded = false;
            originalHeight = this.height;
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height, weight);
            originalHeight = this.height;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            originalHeight = this.height;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
            originalHeight = this.height;
        }

        public LayoutParams(LinearLayout.LayoutParams source) {
            super(source);
            originalHeight = this.height;
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
            originalHeight = this.height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
