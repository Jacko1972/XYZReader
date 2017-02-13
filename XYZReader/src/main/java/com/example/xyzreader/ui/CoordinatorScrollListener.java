package com.example.xyzreader.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.xyzreader.R;

@SuppressWarnings("unused")
public class CoordinatorScrollListener extends FloatingActionButton.Behavior {

    private final Context mContext;

    public CoordinatorScrollListener(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        if (nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL) {
            Animation utils = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
            child.startAnimation(utils);
            child.setVisibility(View.INVISIBLE);
        }
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        Animation in = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        child.startAnimation(in);
        child.setVisibility(View.VISIBLE);

    }
}
