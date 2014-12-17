package com.kageiit.robojava

import org.gradle.api.Project
import org.gradle.api.UnknownProjectException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

public class RobojavaPluginTest {
    static final Project rootProject = ProjectBuilder.builder().withName("root").build()
    static final Project androidProject = createAndroidProject()
    Project testProject
    RobojavaPlugin robojavaPlugin

    @Before
    public void setUp() {
        testProject = ProjectBuilder.builder().withName("test").withParent(rootProject).build()
        robojavaPlugin = new RobojavaPlugin()
    }

    @Test(expected = IllegalStateException.class)
    public void fails_without_android_project_set() {
        robojavaPlugin.apply(testProject)
    }

    @Test(expected = UnknownProjectException.class)
    public void fails_when_android_project_is_not_present() {
        testProject.ext.androidProject = "absent"
        robojavaPlugin.apply(testProject)
    }

    @Test(expected = IllegalStateException.class)
    public void fails_without_android_plugin_applied() {
        Project project = ProjectBuilder.builder().withName("java").withParent(rootProject).build()
        testProject.ext.androidProject = "java"
        robojavaPlugin.apply(testProject)
    }

    @Test(expected = IllegalStateException.class)
    public void fails_without_robolectric_plugin_applied() {
        Project project = ProjectBuilder.builder().withName("norobo").withParent(rootProject).build()
        project.apply plugin: 'com.android.library'
        testProject.ext.androidProject = "norobo"
        robojavaPlugin.apply(testProject)
    }

    @Test
    public void succeeds_with_android_plugin_applied() {
        testProject.ext.androidProject = "android"
        robojavaPlugin.apply(testProject)
    }

    private static Project createAndroidProject() {
        Project project = ProjectBuilder.builder()
                .withName("android")
                .withProjectDir(new File("src/test/fixtures/android_app"))
                .withParent(rootProject)
                .build()

        project.buildscript {
            repositories {
                jcenter()
            }
            dependencies {
                classpath 'com.android.tools.build:gradle:1.0.0'
                classpath 'org.robolectric:robolectric-gradle-plugin:0.14.+'
            }
        }

        project.apply plugin: 'com.android.application'
        project.apply plugin: 'robolectric'

        project.android {
            compileSdkVersion 21
            buildToolsVersion '21.1.2'
        }

        project.evaluate()
        return project
    }
}
