plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

allprojects {
    apply {
        plugin("java")
        plugin("maven-publish")
        plugin("com.github.johnrengelman.shadow")
    }

    group = "io.tofpu.speedbridge2"
    version = "1.1.4-" + "git rev-parse --short=8 HEAD".runCommand(rootDir)

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }

    tasks {
        compileJava {
            options.encoding = "UTF-8"
            options.compilerArgs.plusAssign("-parameters")
            sourceCompatibility = "8"
            targetCompatibility = "8"
        }

        build {
            dependsOn("shadowJar")
        }

        processResources {
            filesMatching("plugin.yml") {
                expand(project.properties)
            }
        }

        getByName<Test>("test") {
            useJUnitPlatform()
        }
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
