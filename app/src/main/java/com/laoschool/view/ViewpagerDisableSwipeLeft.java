package com.laoschool.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.laoschool.screen.HomeActivity;

/**
 * Created by Hue on 2/26/2016.
 */
public class ViewpagerDisableSwipeLeft extends ViewPager {

    private float initialXValue;
    private HomeActivity.SwipeDirection direction;

    public ViewpagerDisableSwipeLeft(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.direction = HomeActivity.SwipeDirection.all;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.IsSwipeAllowed(event)) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    private boolean IsSwipeAllowed(MotionEvent event) {
        if (this.direction == HomeActivity.SwipeDirection.all) return true;

        if (direction == HomeActivity.SwipeDirection.none)//disable any swipe
            return false;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - initialXValue;
                if (diffX > 0 && direction == HomeActivity.SwipeDirection.right) {
                    // swipe from left to right detected
                    return false;
                } else if (diffX < 0 && direction == HomeActivity.SwipeDirection.left) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    public void setAllowedSwipeDirection(HomeActivity.SwipeDirection direction) {
        this.direction = direction;
    }
}
