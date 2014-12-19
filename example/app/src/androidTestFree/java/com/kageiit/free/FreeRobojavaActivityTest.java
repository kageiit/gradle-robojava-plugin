package com.kageiit.free;

import com.kageiit.test.RobojavaTestBase;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import static org.junit.Assert.assertTrue;

public class FreeRobojavaActivityTest extends RobojavaTestBase {

    FreeRobojavaActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(FreeRobojavaActivity.class).create().get();
    }

    @Test
    public void activityIsNotNull() throws Exception {
        assertTrue(activity != null);
    }

    @Test
    public void awesomeMethodisAwesome() {
        activity.awesomeMethod();
    }
}
