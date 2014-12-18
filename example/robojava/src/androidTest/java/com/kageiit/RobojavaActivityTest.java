package com.kageiit;

import android.app.Activity;

import com.kageiit.test.RobojavaTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertTrue;

@RunWith(RobojavaTestRunner.class)
public class RobojavaActivityTest {

    @Test
    public void testSomething() throws Exception {
        Activity activity = Robolectric.buildActivity(RobojavaActivity.class).create().get();
        assertTrue(activity != null);
    }
}
