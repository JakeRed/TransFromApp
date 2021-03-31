package com.zkh.gradle;

import org.gradle.api.Project

public class PluginExtension {
    String lintXmlPath
    String outputPath

    public PluginExtension(Project project) {
        lintXmlPath = "$project.buildDir/reports/lint-results.xml"
        outputPath = "$project.buildDir/reports/lintCleanerLog.txt"
    }
}
