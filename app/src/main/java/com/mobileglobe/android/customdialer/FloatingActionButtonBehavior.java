package com.mobileglobe.android.customdialer;

/**
 * Created by dang on 4/26/17.
 */

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar.SnackbarLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Implements custom behavior for the movement of the FAB in response to the Snackbar.
 * Because we are not using the design framework FloatingActionButton widget, we need to manually
 * implement the Material Design behavior of having the FAB translate upward and downward with
 * the appearance and disappearance of a Snackbar.
 */
public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior<FrameLayout> {
    public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FrameLayout child, View dependency) {
        return dependency instanceof SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FrameLayout child,
                                          View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }
}
