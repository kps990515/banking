dependencies {
    implementation(project(":common"))
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.github.f4b6a3:uuid-creator:6.0.0")
    testImplementation(project(":api"))
}

tasks {
    jar {
        enabled = true // JAR 작업 활성화
    }
    withType<JavaCompile> {
        options.annotationProcessorPath = configurations["annotationProcessor"]
    }
}