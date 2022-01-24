// Copyright (c) Facebook, Inc. and its affiliates.

// This source code is licensed under the MIT license found in the
// LICENSE file in the root directory of this source tree.

package com.reactnativecommunity.androidprogressbar;

import javax.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReadableType;
import android.util.Log;
import android.view.View.AccessibilityDelegate;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import androidx.core.view.ViewCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityEvent;
import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Controls an enclosing ProgressBar. Exists so that the ProgressBar can be recreated if
 * the style would change.
 */
/* package */ class ProgressBarContainerView extends FrameLayout {
  private static final int MAX_PROGRESS = 100;

  private @Nullable Integer mColor;
  private boolean mIndeterminate = true;
  private boolean mAnimating = true;
  private double mProgress;
  private @Nullable ProgressBar mProgressBar;

  public ProgressBarContainerView(Context context) {
    super(context);
  }

  public void setStyle(@Nullable String styleName) {
    int style = ReactProgressBarViewManager.getStyleFromString(styleName);
    mProgressBar = ReactProgressBarViewManager.createProgressBar(getContext(), style);
    mProgressBar.setMax(MAX_PROGRESS);
    ViewCompat.setAccessibilityDelegate(
        mProgressBar,
        new AccessibilityDelegateCompat() {
          @Override
          public boolean performAccessibilityAction(View host, int action, Bundle args) {
            if (action == AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS) {
              // Log.w("TESTING::ProgressBarContainerView", "ACTION_ACCESSIBILITY_FOCUS");
              // Log.w("TESTING::ProgressBarContainerView", "args: " + ( args ));
            } else {
              // Log.w("TESTING::ProgressBarContainerView", "action: " + ( action ));
            }
            return super.performAccessibilityAction(host, action, args);
          }

          @Override
          public void onInitializeAccessibilityNodeInfo(
              View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            try  {
              // Log.w("TESTING::ProgressBarContainerView", "info.getRangeInfo().getMax(): " + ( info.getRangeInfo().getMax() ));
              // Log.w("TESTING::ProgressBarContainerView", "info.getRangeInfo().getMin(): " + ( info.getRangeInfo().getMin() ));
              WritableMap data = Arguments.createMap();
              Float minf = info.getRangeInfo().getMin();
              int min = minf.intValue();
              Float maxf = info.getRangeInfo().getMax();
              int max = maxf.intValue();
              data.putInt("min", min);
              data.putInt("max", max);
              host.setTag(R.id.accessibility_value, data);
            } catch(Exception e) {
              // Log.w("TESTING::ProgressBarContainerView", "e: " + ( e ));
            }
          }

          @Override
          public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setItemCount(10);
            event.setCurrentItemIndex(22);
            // Set item count and current item index on accessibility events for adjustable
            // in order to make Talkback announce the value of the adjustable
            //final ReadableMap accessibilityValue = (ReadableMap) host.getTag(R.id.accessibility_value);
            //if (accessibilityValue != null
            //    && accessibilityValue.hasKey("min")
            //    && accessibilityValue.hasKey("max")) {
            //    final int minDynamic = accessibilityValue.getInt("min");
            //    final int maxDynamic = accessibilityValue.getInt("max");

            //    }
          }

        });
    removeAllViews();
    addView(
        mProgressBar,
        new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT));
  }

  public void setColor(@Nullable Integer color) {
    this.mColor = color;
  }

  public void setIndeterminate(boolean indeterminate) {
    mIndeterminate = indeterminate;
  }

  public void setProgress(double progress) {
    mProgress = progress;
  }

  public void setAnimating(boolean animating) {
    mAnimating = animating;
  }

  public void apply() {
    if (mProgressBar == null) {
      throw new JSApplicationIllegalArgumentException("setStyle() not called");
    }

    mProgressBar.setIndeterminate(mIndeterminate);
    setColor(mProgressBar);
    mProgressBar.setProgress((int) (mProgress * MAX_PROGRESS));
    if (mAnimating) {
      mProgressBar.setVisibility(View.VISIBLE);
    } else {
      mProgressBar.setVisibility(View.GONE);
    }
  }

  private void setColor(ProgressBar progressBar) {
    Drawable drawable;
    if (progressBar.isIndeterminate()) {
      drawable = progressBar.getIndeterminateDrawable();
    } else {
      drawable = progressBar.getProgressDrawable();
    }

    if (drawable == null) {
      return;
    }

    if (mColor != null) {
      drawable.setColorFilter(mColor, PorterDuff.Mode.SRC_IN);
    } else {
      drawable.clearColorFilter();
    }
  }
}
