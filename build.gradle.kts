val bootJar: org.springframework.boot.gradle.tasks.bundling.BootJar by tasks

bootJar.enabled = false

plugins {
	java
	id("org.springframework.boot") version "3.3.6"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.kakaobank"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
	}
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "org.springframework.boot")

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter")
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("org.springframework.boot:spring-boot-starter-validation")
		implementation("org.mapstruct:mapstruct:1.5.5.Final")
		annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		compileOnly ("org.projectlombok:lombok")
		annotationProcessor ("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}