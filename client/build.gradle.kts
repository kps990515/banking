dependencies {
    implementation(project(":common"))
    implementation(project(":mock"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.3")
    implementation("io.github.resilience4j:resilience4j-circuitbreaker:2.2.0")
    implementation("io.github.resilience4j:resilience4j-feign:2.1.0") // Resilience4j Feign 통합
    implementation("io.github.resilience4j:resilience4j-core:2.2.0")
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")
    implementation("io.github.openfeign:feign-jackson:13.5")
}

tasks {
    jar {
        enabled = true // JAR 작업 활성화
    }
}
