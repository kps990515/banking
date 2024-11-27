dependencies {
    // jackson설정
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")

    // FeignException 지원
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.3")

    // SLF4J 로깅
    implementation("org.slf4j:slf4j-api") // SLF4J API
    implementation("ch.qos.logback:logback-classic") // 기본 로거 (Logback)

    // Resilience4j Core
    implementation("io.github.resilience4j:resilience4j-retry:2.2.0") // Retry 기능
    implementation("io.github.resilience4j:resilience4j-circuitbreaker:2.2.0")
}

tasks {
    jar {
        enabled = true // JAR 작업 활성화
    }
}