package com.vergiliy.wedding;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

public class BaseListener {

    // Get main Activity
    protected BaseActivity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof BaseActivity) {
                return (BaseActivity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
