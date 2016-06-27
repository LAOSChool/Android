package com.laoschool.screen.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Tran An on 6/16/2016.
 */
public class MyEditText extends EditText {

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public interface MyEditTextListener {
        void onHideKeyboard();
    }

    private MyEditTextListener listener;



    public void setListener(MyEditTextListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // User has pressed Back key. So hide the keyboard
//            InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
            // TODO: Hide your view as you do it in your activity
            listener.onHideKeyboard();
        }
        return false;
    }
}
