package net.anzix.imprempta.gradle

import org.gradle.api.Project
import org.junit.Test


class DefaultTaskTest {

    @Test
    public void greeterPluginAddsGreetingTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'greeting'

        assertTrue(project.tasks.hello instanceof GreetingTask)
    }
}
