plugins {
    `java-library`
}

subprojects {
    apply(plugin = "java-library")

    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        val implementation by configurations
        implementation("org.jetbrains:annotations:24.0.0")
        testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

        compileOnly("org.projectlombok:lombok:1.18.26")
        testImplementation("org.projectlombok:lombok:1.18.26")
        annotationProcessor("org.projectlombok:lombok:1.18.26")

        testImplementation("org.mockito:mockito-core:5.3.1")
        testImplementation("org.mockito:mockito-junit-jupiter:5.3.1")
    }

    tasks.named<Test>("test") {
        // Use JUnit Platform for unit tests.
        useJUnitPlatform()
    }
}