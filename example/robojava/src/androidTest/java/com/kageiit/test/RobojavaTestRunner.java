package com.kageiit.test;

import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.res.Fs;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.IOException;

/**
 * Custom test runner which is needed if your tests need resources etc.
 */
public class RobojavaTestRunner extends RobolectricTestRunner {

    private static final String PROJECT_DIR = getProjectDirectory();
    private static final String MANIFEST_PROPERTY = PROJECT_DIR + "src/main/AndroidManifest.xml";
    private static final String RES_PROPERTY = PROJECT_DIR + "build/intermediates/res/debug/";
    private static final int TARGET_SDK_VERSION = 18;
    private static final AndroidManifest sAndroidManifest = getAndroidManifest();

    public RobojavaTestRunner(final Class<?> testClass) throws Exception {
        super(testClass);
    }

    @Override
    public AndroidManifest getAppManifest(Config config) {
        return sAndroidManifest;
    }

    private static AndroidManifest getAndroidManifest() {
        return new AndroidManifest(Fs.fileFromPath(MANIFEST_PROPERTY), Fs.fileFromPath(RES_PROPERTY)) {
            @Override
            public int getTargetSdkVersion() {
                return TARGET_SDK_VERSION;
            }
        };
    }

    /**
     * Unfortunately this step is required so that tests can run both from ide and the commandline.
     * Robolectric has difficulty recognizing the manifest file from relative paths.
     *
     * @return The working directory from which tests are run.
     */
    private static String getProjectDirectory() {
        String path = "";
        try {
            File file = new File(".");
            path = file.getCanonicalPath();
            path = replaceLast(path, "/robojava", ""); //android project
            path = replaceLast(path, "/tdd", ""); //stub project
            path = path + "/robojava/";
        } catch (IOException ignored) {
        }
        return path;
    }

    private static String replaceLast(String string, String from, String to) {
        int lastIndex = string.lastIndexOf(from);
        if (lastIndex < 0) {
            return string;
        }
        String tail = string.substring(lastIndex).replaceFirst(from, to);
        return string.substring(0, lastIndex) + tail;
    }
}
