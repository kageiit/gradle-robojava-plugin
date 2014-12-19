package com.kageiit.free;

import android.app.Activity;
import android.os.Bundle;

import com.kageiit.R;

public class FreeRobojavaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.robojava);
    }

    public void awesomeMethod() {
        for (int i = 0; i < 10; i++) {
            String test = "test";
        }
    }
}
