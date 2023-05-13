dependencies {
    implementation("org.hibernate.orm:hibernate-core:6.2.2.Final")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.h2database:h2:2.1.214")
    testImplementation("org.mockito:mockito-core:5.3.1")
    testImplementation("org.mockito:mockito-junit-jupiter:5.3.1")

    compileOnly("org.projectlombok:lombok:1.18.26")
    testImplementation("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    implementation("org.yaml:snakeyaml:2.0")
}