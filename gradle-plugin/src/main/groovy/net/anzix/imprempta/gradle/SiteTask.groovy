package net.anzix.imprempta.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class SiteTask extends DefaultTask {

    def String source = '.'

    def String destination = "build/_site"

    @TaskAction
    def greet() {
        net.anzix.imprempta.LegacyStarter.main(project.buildscript.configurations.classpath.asPath, source, destination);
    }

}