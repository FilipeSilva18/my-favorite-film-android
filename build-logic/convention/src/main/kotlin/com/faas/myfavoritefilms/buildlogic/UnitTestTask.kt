package com.faas.myfavoritefilms.buildlogic

import org.gradle.api.Project

internal fun Project.registerUnitTestTask(testTaskName: String) {
    tasks.register("unitTest") {
        group = "verification"
        description = "Runs this module's JVM unit tests ($testTaskName)."
        dependsOn(testTaskName)
    }
}
