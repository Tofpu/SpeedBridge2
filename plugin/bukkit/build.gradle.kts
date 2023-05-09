plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    java
}

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    implementation(project(":core"))

    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}