plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.dependency.management)
    id("checkstyle")
    id("jacoco")
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
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)

    // Database
    runtimeOnly(libs.postgresql)
    implementation(libs.liquibase.core)

    //// Tools
    // Lombok
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testImplementation(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    // Mapstruct
    compileOnly(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)
    testImplementation(libs.mapstruct)
    testAnnotationProcessor(libs.mapstruct.processor)

    implementation(libs.jjwt)
    implementation(libs.jjwt.impl)
    implementation(libs.jjwt.jackson)

    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    // Testing
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)
    testImplementation(libs.rest.assured)
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.h2.database)
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

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

val unitTestCoverageMinimum = "0.8"
val e2eTestCoverageMinimum = "0.7"
val integrationTestCoverageMinimum = "0.0" // TODO fix after creating tests
val totalCoverageMinimum = "0.7"

fun JacocoCoverageVerification.configureViolationRules(
    coverageMinimum: String,
) {
    violationRules {
        rule {
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = coverageMinimum.toBigDecimal()
            }
        }
    }
}

val commonClassDirectories =
    fileTree(layout.buildDirectory.dir("classes/java/main")) {
        exclude(
            "**/config/**",
            "**/exception/**",
            "**/web/mapper/**",
            "**/*Response.class",
            "**/*Request.class",
            "**/*Dto.class",
            "**/*Entity.class",
            "**/*Properties.class",
            "**/*Application.class",
        )
    }

val unitTestClassDirectories = commonClassDirectories.matching {
    exclude(
        "**/web/controller/ControllerAdvice.class",
    )
}

val e2eTestClassDirectories = commonClassDirectories.matching {
    exclude(

    )
}

val integrationTestClassDirectories = commonClassDirectories.matching {
    exclude(

    )
}

val commonSourceDirectories = files("src/main/java")

// Unit test
val unitTest by tasks.registering(Test::class) {
    description = "Runs the unit tests."
    group = "verification"

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath

    useJUnitPlatform {
        includeTags("unit")
    }

    finalizedBy("jacocoUnitTestReport")
}

val unitExecutionData = layout.buildDirectory.file("jacoco/unitTest.exec")

tasks.create("jacocoUnitTestReport", JacocoReport::class.java) {
    dependsOn(unitTest)

    reports {
        xml.required.set(false)
        html.required.set(true)
        html.outputLocation.set(
            layout.buildDirectory
                .dir("reports/jacoco/unitTest/html")
        )
    }

    classDirectories.setFrom(unitTestClassDirectories)
    sourceDirectories.setFrom(commonSourceDirectories)
    executionData.setFrom(unitExecutionData)
}

tasks.create(
    "jacocoUnitTestCoverageVerification",
    JacocoCoverageVerification::class.java
) {
    dependsOn("jacocoUnitTestReport")

    configureViolationRules(unitTestCoverageMinimum)

    classDirectories.setFrom(unitTestClassDirectories)
    executionData.setFrom(unitExecutionData)
}


// E2E test
val e2eTest by tasks.registering(Test::class) {
    description = "Runs the e2e tests."
    group = "verification"

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath

    useJUnitPlatform {
        includeTags("e2e")
    }

    finalizedBy("jacocoE2ETestReport")
}

val e2eExecutionData = layout.buildDirectory.file("jacoco/e2eTest.exec")

tasks.create("jacocoE2ETestReport", JacocoReport::class.java) {
    dependsOn(e2eTest)

    reports {
        xml.required.set(false)
        html.required.set(true)
        html.outputLocation.set(
            layout.buildDirectory
                .dir("reports/jacoco/e2eTest/html")
        )
    }

    classDirectories.setFrom(e2eTestClassDirectories)
    sourceDirectories.setFrom(commonSourceDirectories)
    executionData.setFrom(e2eExecutionData)
}

tasks.create(
    "jacocoE2ETestCoverageVerification",
    JacocoCoverageVerification::class.java
) {
    dependsOn("jacocoE2ETestReport")

    configureViolationRules(e2eTestCoverageMinimum)

    classDirectories.setFrom(e2eTestClassDirectories)
    executionData.setFrom(e2eExecutionData)
}


// Integration test
val integrationTest by tasks.registering(Test::class) {
    description = "Runs the integration tests."
    group = "verification"

    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath

    useJUnitPlatform {
        includeTags("integration")
    }

    finalizedBy("jacocoIntegrationTestReport")
}

val integrationExecutionData =
    layout.buildDirectory.file("jacoco/integrationTest.exec")

tasks.create("jacocoIntegrationTestReport", JacocoReport::class.java) {
    dependsOn(integrationTest)

    reports {
        xml.required.set(false)
        html.required.set(true)
        html.outputLocation.set(
            layout.buildDirectory
                .dir("reports/jacoco/integrationTest/html")
        )
    }

    classDirectories.setFrom(integrationTestClassDirectories)
    sourceDirectories.setFrom(commonSourceDirectories)
    executionData.setFrom(integrationExecutionData)
}

tasks.create(
    "jacocoIntegrationTestCoverageVerification",
    JacocoCoverageVerification::class.java
) {
    dependsOn("jacocoIntegrationTestReport")

    configureViolationRules(integrationTestCoverageMinimum)

    classDirectories.setFrom(integrationTestClassDirectories)
    executionData.setFrom(integrationExecutionData)
}


// All test
tasks.test {
    useJUnitPlatform()
}

val testExecutionData = layout.buildDirectory.file("jacoco/test.exec")

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(false)
        html.required.set(true)
        html.outputLocation.set(
            layout.buildDirectory
                .dir("reports/jacoco/test/html")
        )
    }

    classDirectories.setFrom(commonClassDirectories)
    sourceDirectories.setFrom(commonSourceDirectories)
    executionData.setFrom(testExecutionData)
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)

    configureViolationRules(totalCoverageMinimum)

    classDirectories.setFrom(tasks.jacocoTestReport.get().classDirectories)
    executionData.setFrom(tasks.jacocoTestReport.get().executionData)
}


tasks.build {
    dependsOn("jacocoTestCoverageVerification")
}
