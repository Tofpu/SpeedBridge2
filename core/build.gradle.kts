dependencies {
    api("org.hibernate:hibernate-core:5.6.15.Final")
    api("org.reflections:reflections:0.10.2")
    api("com.h2database:h2:2.1.214")
    api("com.github.Tofpu:dynamic-configuration:1.0.2") {
        isTransitive = false
    }
    api("org.spongepowered:configurate-yaml:4.0.0")
    implementation("com.mysql:mysql-connector-j:8.1.0")
}