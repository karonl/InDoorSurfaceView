package com.karonl.surfaceinstance;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.junit.Test;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


    public void testNum() throws Exception {
        final int expected = 1;
        final int reality = 10;
        assertEquals(expected, reality);
    }
}