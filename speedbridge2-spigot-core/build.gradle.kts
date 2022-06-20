plugins {
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

dependencies {
    implementation(project(":speedbridge2-common"))

    compileOnly("org.xerial:sqlite-jdbc:3.36.0.3")
    compileOnly("com.sk89q:worldedit:6.0.0-SNAPSHOT")

    implementation("com.github.Revxrsal.Lamp:bukkit:3.0.7")
    implementation("net.kyori:adventure-platform-bukkit:4.0.1")

    implementation("com.github.tofpu:DynamicClass:1.1") {
        exclude("com.google.guava", "guava")
    }

    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("org.spongepowered:configurate-hocon:4.1.2")
    implementation("commons-lang:commons-lang:2.6")
    implementation("commons-io:commons-io:2.11.0")

    implementation("org.bstats:bstats-bukkit:3.0.0")

    compileOnly("me.clip:placeholderapi:2.10.10")

    implementation("com.github.ben-manes.caffeine:caffeine:2.9.3")

    implementation("com.github.cryptomorin:XSeries:8.7.1")
    implementation("com.github.tofpu.MultiWorldEdit:multiworldedit-api:0eb85d6cbd") {
        exclude("de.schlichtherle", "truezip")
        exclude("rhino", "js")
        exclude("com.sk89q", "worldedit")
    }

    implementation("com.github.tofpu:umbrella:1.0.2")
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        mergeServiceFiles()
        classifier = ""

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
            relocate("org.yaml.snakeyaml", "io.tofpu.speedbridge2.lib.snakeyml")
            relocate("revxrsal", "io.tofpu.speedbridge2.lib.lamp")
        }

        exclude("META-INF/**")
    }

    runServer {
        minecraftVersion("1.8.8")
        pluginJars(project.file("libs/worldedit-bukkit-6.1.jar"))
    }
}