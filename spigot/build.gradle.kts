import xyz.jpenilla.runpaper.task.RunServer

plugins {
    id("xyz.jpenilla.run-paper") version "2.0.1"
}

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

    compileOnly("org.xerial:sqlite-jdbc:3.36.0.3")
    compileOnly("com.sk89q:worldedit:6.0.0-SNAPSHOT")

    implementation("com.github.Revxrsal.Lamp:common:3.2.1")
    implementation("com.github.Revxrsal.Lamp:bukkit:3.2.1")

    implementation("net.kyori:adventure-api:4.10.1")
    implementation("net.kyori:adventure-text-minimessage:4.10.1")
    implementation("net.kyori:adventure-platform-bukkit:4.0.1")

    implementation("com.github.tofpu:DynamicClass:1.1") {
        exclude("com.google.guava", "guava")
    }

    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("org.spongepowered:configurate-hocon:4.1.2")
    implementation("commons-lang:commons-lang:2.6")
    implementation("commons-io:commons-io:2.11.0")

    implementation("org.bstats:bstats-bukkit:3.0.0")

    compileOnly("me.clip:placeholderapi:2.11.6")

    implementation("com.github.ben-manes.caffeine:caffeine:2.9.3")

    implementation("com.github.cryptomorin:XSeries:14.0.0")
    implementation("com.github.tofpu.MultiWorldEdit:multiworldedit-api:b4b6444c20") {
        exclude("de.schlichtherle", "truezip")
        exclude("rhino", "js")
        exclude("com.sk89q", "worldedit")
    }

    implementation("com.tofpu.toolbar:toolbar-core:3.0.0")
    implementation("com.github.Tofpu:dynamicclass:1.3") {
        exclude("com.google.guava", "guava")
    }
}

tasks {
    shadowJar {
        archiveFileName.set("Speedbridge2-${version}-all.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        relocate("org.xerial", "io.tofpu.speedbridge2.lib.sqlite")
        relocate("javax.persistence", "io.tofpu.speedbridge2.lib.javax")
        relocate("io.tofpu.dynamicclass", "io.tofpu.speedbridge2.lib.dynamicclass")
        relocate("org.spongepowered", "io.tofpu.speedbridge2.lib.configurate")
        relocate("net.kyori.adventure", "io.tofpu.speedbridge2.lib.adventure")
        relocate("com.zaxxer.HikariCP", "io.tofpu.speedbridge2.lib.hikaricp")
        relocate("org.bstats", "io.tofpu.speedbridge2.lib.bstats")
        relocate("com.github.benmanes.caffeine", "io.tofpu.speedbridge2.lib.caffeine")
        relocate("org.apache.commons", "io.tofpu.speedbridge2.lib.commons")
        relocate("org.yaml.snakeyaml", "io.tofpu.speedbridge2.lib.snakeyml")
        relocate("revxrsal", "io.tofpu.speedbridge2.lib.lamp")

        exclude("META-INF/**")
    }

    val projectJarTask = named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar")

    val minecraftVersions = listOf("1.12.2", "1.19.4", "26.2")
    val useDirectJars = listOf("1.12.2")
    minecraftVersions.forEach { mcVersion ->
        val capitalizedVersion = mcVersion.replace(".", "_")

        register<RunServer>("runServer_$capitalizedVersion") {
            // Ensure the plugin builds before the server starts
            dependsOn(projectJarTask)

            group = "run paper"
            description = "Runs a Paper test server for Minecraft $mcVersion"

            runDirectory.set(project.layout.projectDirectory.dir("run/$mcVersion"))
            minecraftVersion(mcVersion)
            if (useDirectJars.contains(mcVersion)) {
                serverJar(file(project.file("libs/$mcVersion/paper.jar")))
            }

            // Add your own project's built plugin JAR
            pluginJars(projectJarTask.flatMap { it.archiveFile })

            val externalJars = project.file("libs/$mcVersion/plugins").listFiles { file -> file.extension == "jar" } ?: emptyArray()
            // Spread operator (*) passes them as individual arguments
            pluginJars(
                *externalJars
            )
        }
    }

    withType<JavaCompile> {
        options.apply {
            compilerArgs.add("-AlistenerPackage=io.tofpu.speedbridge2.generated")
            compilerArgs.addAll(listOf("-processor", "com.tofpu.toolbar.processor.ListenerProcessor"))
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
        setSourceCompatibility(JavaVersion.VERSION_1_8)
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }
}