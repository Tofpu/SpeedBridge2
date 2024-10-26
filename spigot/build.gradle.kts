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

    implementation("com.github.cryptomorin:XSeries:8.7.1")
    implementation("com.github.tofpu.MultiWorldEdit:multiworldedit-api:8930fd3caa") {
        exclude("de.schlichtherle", "truezip")
        exclude("rhino", "js")
        exclude("com.sk89q", "worldedit")
    }

    implementation("com.github.Tofpu:toolbar:v1-SNAPSHOT")
    implementation("com.github.Tofpu:dynamicclass:1.3") {
        exclude("com.google.guava", "guava")
    }
}

tasks {
    shadowJar {
        archiveName = "Speedbridge2-${version}-all.jar"

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
//        minecraftVersion("1.8.8")
//        pluginJars(project.file("libs/worldedit-bukkit-6.1.jar"))
//        minecraftVersion("1.20.4")
        minecraftVersion("1.19.4")
        pluginJars(project.file("libs/worldedit-bukkit-7.3.0.jar"))
    }
}
