package com.laoschool.screen.login;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

public class NonFocusingScrollView extends ScrollView {

    public NonFocusingScrollView(Context context) {
        super(context);
    }

    public NonFocusingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonFocusingScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @Override
    public void requestChildFocus(View child, View focused) {
        if (focused instanceof EditText)
            return;
        super.requestChildFocus(child, focused);
    }

}