dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":client"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.3")
    implementation("io.github.resilience4j:resilience4j-circuitbreaker:2.2.0")
    implementation("com.github.f4b6a3:uuid-creator:6.0.0")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks {
    jar {
        enabled = true // JAR 작업 활성화
    }
}
