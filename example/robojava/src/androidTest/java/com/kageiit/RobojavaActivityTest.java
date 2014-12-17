package com.kageiit;

import android.app.Activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class RobojavaActivityTest {

    @Test
    public void testSomething() throws Exception {
        Activity activity = Robolectric.buildActivity(RobojavaActivity.class).create().get();
        assertTrue(activity != null);
    }
}
