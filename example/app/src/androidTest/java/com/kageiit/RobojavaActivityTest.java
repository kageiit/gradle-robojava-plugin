package com.kageiit;

import android.app.Activity;

import com.kageiit.test.RobojavaTestBase;

import org.junit.Test;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertTrue;

public class RobojavaActivityTest extends RobojavaTestBase {

    @Test
    public void testSomething() throws Exception {
        Activity activity = Robolectric.buildActivity(RobojavaActivity.class).create().get();
        assertTrue(activity != null);
    }
}
