plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0"
    `maven-publish`
}

group = "io.tofpu.speedbridge2"
version = "1.0.7"

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        mergeServiceFiles()
        classifier = "";

        dependencies {
            relocate("org.xerial", "io.tofpu.speedbridge2.lib.sqlite")
            relocate("javax.persistence", "io.tofpu.speedbridge2.lib.javax")
            relocate("io.tofpu.dynamicclass", "io.tofpu.speedbridge2.lib.dynamicclass")
            relocate("org.spongepowered", "io.tofpu.speedbridge2.lib.configurate")
            relocate("net.kyori.adventure", "io.tofpu.speedbridge2.lib.adventure")
            relocate("com.zaxxer.HikariCP", "io.tofpu.speedbridge2.lib.hikaricp")
            relocate("org.bstats", "io.tofpu.speedbridge2.lib.bstats")
            relocate("com.github.benmanes.caffeine", "io.tofpu.speedbridge2.lib.caffeine")
            relocate("org.apache.commons", "io.tofpu.speedbridge2.lib.commons")
        }

        exclude("META-INF/**")
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    compileOnly("org.xerial:sqlite-jdbc:3.36.0.3")

    compileOnly("com.sk89q:worldedit:6.0.0-SNAPSHOT")

    implementation("cloud.commandframework:cloud-bukkit:1.6.2")
    implementation("cloud.commandframework:cloud-annotations:1.6.1")

    implementation("net.kyori:adventure-api:4.10.1")
    implementation("net.kyori:adventure-platform-bukkit:4.0.1")
    implementation("net.kyori:adventure-text-minimessage:4.10.1")

    implementation("com.github.tofpu:DynamicClass:fdc123593b") {
        exclude("com.google.guava", "guava")
    }

    implementation("org.spongepowered:configurate-hocon:4.1.2")
    implementation("commons-lang:commons-lang:2.6")
    implementation("commons-io:commons-io:2.11.0")

    implementation("org.bstats:bstats-bukkit:3.0.0")

    compileOnly("me.clip:placeholderapi:2.10.10")

    implementation("com.github.ben-manes.caffeine:caffeine:3.0.5")

    implementation("com.github.cryptomorin:XSeries:8.6.1")
    implementation("com.github.tofpu.MultiWorldEdit:multiworldedit-api:0eb85d6cbd") {
        exclude("de.schlichtherle", "truezip")
        exclude("rhino", "js")
        exclude("com.sk89q", "worldedit")
    }
}
