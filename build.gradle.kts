plugins {
    id("java")
//    id("com.palantir.java-format") version "2.50.0"
}

group = "io.tofpu.speedbridge2"
version = "3.0.0-SNAPSHOT"

subprojects {
    apply(plugin = "java")
//    apply(plugin = "com.palantir.java-format")

    group = "io.tofpu.speedbridge2"
    version = "3.0.0-SNAPSHOT"

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}