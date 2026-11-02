plugins {
    alias(libs.plugins.movies.jvm.library)
}

dependencies {
    testImplementation(libs.konsist)
}

tasks.withType<Test>().configureEach {
    outputs.upToDateWhen { false }
}
