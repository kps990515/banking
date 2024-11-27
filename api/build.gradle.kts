dependencies {
    implementation(project(":service"))
    implementation(project(":common"))
    implementation(project(":client"))
    implementation(project(":domain"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.3")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

tasks {
    bootJar {
        mainClass.set("org.com.kakaobank.api.Application")
    }
    jar {
        enabled = false
    }
}