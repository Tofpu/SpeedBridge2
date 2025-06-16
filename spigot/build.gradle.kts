plugins {
    id("java")
    id("com.diffplug.spotless") version "7.0.0.BETA4"
    id("org.jooq.jooq-codegen-gradle") version "3.19.24"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "9.0.0-beta16"
}

group = "io.tofpu.speedbridge2"
version = "3.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    maven("https://jitpack.io")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("com.sk89q:worldedit:6.0.0-SNAPSHOT")
    compileOnly("org.immutables:value:2.10.1")
    compileOnly("org.jetbrains:annotations:24.0.1")
    annotationProcessor("org.immutables:value:2.10.1")

    implementation("com.github.tofpu.MultiWorldEdit:multiworldedit-api:8930fd3caa") {
        exclude("com.sk89q", "worldedit")
    }
    implementation("commons-io:commons-io:2.11.0")
    implementation("com.github.tofpu.toolbar:toolbar-api:3793d5f149")
    implementation("space.arim.dazzleconf:dazzleconf-ext-snakeyaml:1.3.0-M2")
    implementation("com.github.cryptomorin:XSeries:13.3.1")
    implementation("io.github.revxrsal:lamp.common:4.0.0-rc.12")
    implementation("io.github.revxrsal:lamp.bukkit:4.0.0-rc.12")
    implementation("net.kyori:adventure-api:4.10.1")
//    implementation("net.kyori:adventure-text-minimessage:4.10.1")
    implementation("net.kyori:adventure-platform-bukkit:4.0.1")

    implementation("org.jooq:jooq:3.19.24")
    implementation("org.jooq:jooq-meta:3.19.24")
    implementation("org.jooq:jooq-codegen:3.19.24")

    // Code generation specific dependencies, like JDBC drivers, codegen extensions, etc.
    jooqCodegen("org.jooq:jooq-meta-extensions:3.19.24")

    // h2 driver
    implementation("com.h2database:h2:2.3.232")

    implementation("com.github.Revxrsal:EventBus:1.3")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

jooq {
    configuration {
        generator {
            database {
                name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                properties {
                    property {
                        key = "scripts"
                        value = "src/main/resources/schema.sql"
                    }
                    property {
                        key = "sort"
                        value = "semantic"
                    }
                    property {
                        key = "unqualifiedSchema"
                        value = "none"
                    }
                    property {
                        key = "defaultCaseName"
                        value = "as_is"
                    }
                    property {
                        key = "logExecutedQueries"
                        value = "true"
                    }
                    property {
                        key = "logExecutionResults"
                        value = "true"
                    }
                }
            }
        }
    }
}

tasks.named("compileJava") {
    dependsOn("jooqCodegen")
}

spotless {
//    ratchetFrom("origin/main")

    format("misc") {
        target("*.gradle", "*.md", ".gitignore")

        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }

    java {
        formatAnnotations()
        removeUnusedImports()
        palantirJavaFormat()
    }
}

tasks {
    compileJava {
        options.compilerArgs.add("-parameters")
    }

    test {
        useJUnitPlatform()
    }

    processResources {
        expand(project.properties)
    }

    runServer {
        val minecraftVersion = "1.19.4"
        minecraftVersion(minecraftVersion)
        runDirectory.set(project.layout.projectDirectory.dir("run/${minecraftVersion}"))

        downloadPlugins {
            url("https://dev.bukkit.org/projects/worldedit/files/5145924/download")
        }
    }

    shadowJar {
        archiveFileName.set("${project.name}-${project.version}.jar")

        mergeServiceFiles()
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}