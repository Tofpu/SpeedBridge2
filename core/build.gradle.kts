plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    java
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")

    implementation("org.hibernate.orm:hibernate-core:6.2.2.Final")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.h2database:h2:2.1.214")
    testImplementation("org.mockito:mockito-core:5.3.1")

    testImplementation("org.mockito:mockito-junit-jupiter:5.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}