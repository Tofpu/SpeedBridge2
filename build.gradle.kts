plugins {
    java
}

subprojects {
    apply(plugin = "java")

    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        val implementation by configurations
        implementation("org.jetbrains:annotations:24.0.0")
        testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    }

    tasks.named<Test>("test") {
        // Use JUnit Platform for unit tests.
        useJUnitPlatform()
    }
}