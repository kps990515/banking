dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")
}

tasks {
    bootJar {
        mainClass.set("org.com.kakaobank.mock.MockApplication")
    }
    jar {
        enabled = true // JAR 작업 활성화
    }
}