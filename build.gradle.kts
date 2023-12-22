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
    version = "1.1.3-" + "git rev-parse --short=8 HEAD".runCommand(rootDir)

    repositories {
        mavenLocal()
        mavenCentral()

        maven("https://jitpack.io")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://maven.enginehub.org/repo/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://repo.codemc.org/repository/maven-public/")
        maven("https://libraries.minecraft.net")
    }

    dependencies {
        compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

        implementation("com.github.Revxrsal.Lamp:common:3.1.7")

        implementation("net.kyori:adventure-api:4.10.1")
        implementation("net.kyori:adventure-text-minimessage:4.10.1")

        implementation("com.github.tofpu:DynamicClass:1.1") {
            exclude("com.google.guava", "guava")
        }

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    }

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
