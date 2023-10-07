plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io/")
    }

    group = "com.github.tofpu.speedbridge2"
    version = "2.0.0-alpha-" + "git rev-parse --short=8 HEAD".runCommand(rootDir)

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

    java {
        this.sourceCompatibility = JavaVersion.VERSION_1_8
    }
}

fun String.runCommand(
        workingDir: File = File("."),
        timeoutAmount: Long = 60,
        timeoutUnit: TimeUnit = TimeUnit.SECONDS
): String = ProcessBuilder(split("\\s(?=(?:[^'\"`]*(['\"`])[^'\"`]*\\1)*[^'\"`]*$)".toRegex()))
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
        .apply { waitFor(timeoutAmount, timeoutUnit) }
        .run {
            val error = errorStream.bufferedReader().readText().trim()
            if (error.isNotEmpty()) {
                throw Exception(error)
            }
            inputStream.bufferedReader().readText().trim()
        }