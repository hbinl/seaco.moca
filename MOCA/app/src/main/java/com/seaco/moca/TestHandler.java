package com.seaco.moca;

import android.app.Activity;
import android.widget.RelativeLayout;
import android.view.View;

/**
 * Created by HaoBin on 15/01/16.
 */
public class TestHandler {

    public Activity activity;
    static RelativeLayout container;

    public TestHandler(Activity _activity) {
        this.activity = _activity;
        container = (RelativeLayout) activity.findViewById(R.id.moca_tests_container);
    }

    public View getCurrentActivity() {
        return container.getChildAt(0);
    }

    public void section13() {

    }
}
