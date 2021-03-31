package com.zkh.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * 清除项目无用资源的插件
 */
public class ResCleanPlugin implements Plugin<Project> {
    final String TASKNAME = "clearLintTask"
    final String ExtensionName = "lintCleaner"


    @Override
    void apply(Project project) {
        project.extensions.create(ExtensionName, PluginExtension, project)
        Task task = project.tasks.create(TASKNAME, CleanTask)
        task.dependsOn project.tasks.getByName('lint')

    }
}