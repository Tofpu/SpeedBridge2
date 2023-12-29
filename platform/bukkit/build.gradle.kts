plugins {
    id("xyz.jpenilla.run-paper") version "2.0.1"
}

configurations {
    all {
        exclude("javax.persistence", "persistence-api")
//        exclude("org.yaml", "snakeyaml")

//        resolutionStrategy {
//            force("org.yaml:snakeyaml:1.15")
//        }
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
    implementation(project(":common"))

    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    testImplementation("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    testImplementation("org.yaml:snakeyaml") {
        version {
            strictly("1.15")
        }
    }

    compileOnly("com.sk89q:worldedit:6.0.0-SNAPSHOT")
    testImplementation("com.sk89q:worldedit:6.0.0-SNAPSHOT")
    implementation("com.github.tofpu.MultiWorldEdit:multiworldedit-api:0eb85d6cbd") {
        exclude("de.schlichtherle", "truezip")
        exclude("rhino", "js")
        exclude("com.sk89q", "worldedit")
    }

    implementation("com.github.Revxrsal.Lamp:common:3.1.8")
    implementation("com.github.Revxrsal.Lamp:bukkit:3.1.8")

    testImplementation("com.github.Tofpu:v1.8-mockbukkit:v1.8-spigot-SNAPSHOT")
}

tasks.shadowJar {
    archiveBaseName.set("speedbridge2")
    archiveClassifier.set("")
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