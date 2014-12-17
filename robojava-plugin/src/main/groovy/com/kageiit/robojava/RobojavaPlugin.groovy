package com.kageiit.robojava

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.tasks.testing.Test
import org.robolectric.gradle.RobolectricPlugin

class RobojavaPlugin implements Plugin<Project> {

    public static final String COBERTURA_PLUGIN_CLASS_NAME = "net.saliman.gradle.plugin.cobertura.CoberturaPlugin"

    @Override
    void apply(Project robojavaProject) {

        if (!robojavaProject.ext.has('androidProject')) {
            throw new IllegalStateException("ext.androidProject must be defined before applying plugin.")
        }

        Project androidProject = robojavaProject.rootProject.project(":${robojavaProject.ext.androidProject}")

        def plugins = androidProject.plugins
        def hasAppPlugin = plugins.hasPlugin(AppPlugin)
        def hasLibraryPlugin = plugins.hasPlugin(LibraryPlugin)
        def hasRobolectricPlugin = plugins.hasPlugin(RobolectricPlugin)

        def androidPlugin
        if (hasAppPlugin) {
            androidPlugin = plugins.getPlugin(AppPlugin)
        } else if (hasLibraryPlugin) {
            androidPlugin = plugins.getPlugin(LibraryPlugin)
        } else {
            throw new IllegalStateException("${androidProject.name} is not an android project.")
        }

        if (!hasRobolectricPlugin) {
            throw new IllegalStateException("Robolectric gradle plugin needs to be applied on ${androidProject.name}.")
        }

        robojavaProject.apply plugin: 'java'
        robojavaProject.repositories { RepositoryHandler repositoryHandler ->
            repositoryHandler.addAll(androidProject.repositories)
        }

        def android = androidProject.android
        robojavaProject.sourceSets.test.java.srcDirs = android.sourceSets.androidTest.java.srcDirs
        robojavaProject.sourceSets.test.resources.srcDirs = android.sourceSets.androidTest.resources.srcDirs

        def variants = hasAppPlugin ? android.applicationVariants : android.libraryVariants
        def testVariant = variants.find({ it.name == android.testBuildType })

        def androidCompile = androidProject.getConfigurations().getByName('compile').copyRecursive()
        robojavaProject.configurations.add(androidCompile)

        def androidTestCompile = androidProject.getConfigurations().getByName('androidTestCompile').copyRecursive()
        robojavaProject.configurations.add(androidTestCompile)

        robojavaProject.dependencies {
            compile testVariant.javaCompile.outputs.files
            compile testVariant.javaCompile.classpath
            compile androidCompile
            compile 'junit:junit:4.12'
            compile robojavaProject.files(androidPlugin.getBootClasspath())
            testCompile androidTestCompile
        }

        robojavaProject.tasks.withType(Test) {
            outputs.upToDateWhen { false }
            scanForTestClasses = false
            include "**/*Test.class"
        }

        try {
            if (robojavaProject.plugins.hasPlugin(Class.forName(COBERTURA_PLUGIN_CLASS_NAME))) {
                robojavaProject.cobertura {
                    coverageDirs = testVariant.javaCompile.outputs.files.collect { it.toString() }
                    coverageSourceDirs = android.sourceSets.main.java.srcDirs.asList()
                    auxiliaryClasspath += testVariant.javaCompile.classpath
                    coverageExcludes = [".*\\.package-info.*", ".*\\.R.*", ".*BuildConfig.*"]
                }
            }
        } catch (ClassNotFoundException ignored) {
        }
    }
}
