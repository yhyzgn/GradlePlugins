package com.yhy.plugins.aspectj.internal

import com.android.build.gradle.BasePlugin
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.external.cmake.server.Project
import org.gradle.api.DomainObjectCollection
import org.gradle.api.GradleException

class AOPConfig {

    private final Project project
    private final boolean hasAppPlugin
    private final boolean hasLibPlugin
    private final BasePlugin plugin

    AOPConfig(Project p) {
        project = p
        hasAppPlugin = project.plugins.hasPlugin(AppPlugin)
        hasLibPlugin = project.plugins.hasPlugin(LibraryPlugin)

        if (!hasAppPlugin && !hasLibPlugin) {
            throw new GradleException("android-aspjectjx: The 'com.android.application' or 'com.android.library' plugin is required.")
        }
        plugin = project.plugins.getPlugin(hasAppPlugin ? AppPlugin : LibraryPlugin)
    }

    /**
     * Return all variants.
     *
     * @return Collection of variants.
     */
    DomainObjectCollection<BaseVariant> getVariants() {
        return hasAppPlugin ? project.android.applicationVariants : project.android.libraryVariants
    }

    /**
     * Return boot classpath.
     * @return Collection of classes.
     */
    List<File> getBootClasspath() {
        if (project.android.hasProperty('bootClasspath')) {
            return project.android.bootClasspath
        } else {
            return plugin.runtimeJarList
        }
    }
}