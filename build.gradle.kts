plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    id("checkstyle")
}

group = "ru.gozhan"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {

    // Spring boot Starters
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)

    // Database
    runtimeOnly(libs.postgresql)
    implementation(libs.liquibase.core)

    // Tools
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    compileOnly(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)

    implementation(libs.jjwt)
    implementation(libs.jjwt.impl)
    implementation(libs.jjwt.jackson)

    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    // Testing
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.postgresql)
    testRuntimeOnly(libs.junit.platform.launcher)

    // Checkstyle
    checkstyle(libs.checkstyle)

}

checkstyle {
    toolVersion = libs.versions.checkstyle.get()
    configFile = file("config/checkstyle/sun_checks.xml")
    configProperties["org.checkstyle.sun.suppressionfilter.config"] =
        file("config/checkstyle/checkstyle-suppressions.xml").path

    isIgnoreFailures = false
}

tasks.withType<Checkstyle> {
    reports {
        xml.required.set(false)
        html.required.set(true)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
