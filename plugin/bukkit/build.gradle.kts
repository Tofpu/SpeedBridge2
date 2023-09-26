plugins {
    id("xyz.jpenilla.run-paper") version "2.0.1"
}

configurations {
    all {
        exclude("javax.persistence", "persistence-api")
    }
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":logic"))

    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    testImplementation("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    compileOnly("com.sk89q:worldedit:6.0.0-SNAPSHOT")
    testImplementation("com.sk89q:worldedit:6.0.0-SNAPSHOT")
    implementation("com.github.tofpu.MultiWorldEdit:multiworldedit-api:0eb85d6cbd") {
        exclude("de.schlichtherle", "truezip")
        exclude("rhino", "js")
        exclude("com.sk89q", "worldedit")
    }

    implementation("com.github.Revxrsal.Lamp:common:3.1.5")
    implementation("com.github.Revxrsal.Lamp:bukkit:3.1.5")

    implementation("commons-io:commons-io:2.11.0")
}

tasks.shadowJar {
    dependencies {
        relocate("javax.persistence", "com.github.tofpu.speedbridge2.libs.javax")
        relocate("org.yaml", "com.github.tofpu.speedbridge2.libs.snakeyaml")
    }
}

tasks.runServer {
    minecraftVersion("1.8.8")
//    pluginJars(project.file("libs/worldedit-bukkit-7.2.15.jar"))
        pluginJars(project.file("libs/worldedit-bukkit-6.1.jar"))
}