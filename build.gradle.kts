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
    testImplementation(libs.spring.security.test)
    testImplementation(libs.rest.assured)
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.h2.database)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(libs.lombok)
    testAnnotationProcessor(libs.lombok)

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

val unitTestCoverageMinimum = "0.15" // TODO must be 0.8
val integrationTestCoverageMinimum = "0.7" // TODO must be 0.7
val totalCoverageMinimum = "0.7" // TODO must be 0.9

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

    finalizedBy(
        "jacocoUnitTestReport",
        "jacocoUnitTestCoverageVerification",
    )
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

    classDirectories.setFrom(commonClassDirectories)
    sourceDirectories.setFrom(commonSourceDirectories)
    executionData.setFrom(unitExecutionData)
}

tasks.create(
    "jacocoUnitTestCoverageVerification",
    JacocoCoverageVerification::class.java
) {
    dependsOn("jacocoUnitTestReport")

    configureViolationRules(unitTestCoverageMinimum)

    classDirectories.setFrom(commonClassDirectories)
    executionData.setFrom(unitExecutionData)
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

    mustRunAfter(unitTest)

    finalizedBy(
        "jacocoIntegrationTestReport",
        "jacocoIntegrationTestCoverageVerification",
    )
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

    classDirectories.setFrom(commonClassDirectories)
    sourceDirectories.setFrom(commonSourceDirectories)
    executionData.setFrom(integrationExecutionData)
}

tasks.create(
    "jacocoIntegrationTestCoverageVerification",
    JacocoCoverageVerification::class.java
) {
    dependsOn("jacocoIntegrationTestReport")

    configureViolationRules(integrationTestCoverageMinimum)

    classDirectories.setFrom(commonClassDirectories)
    executionData.setFrom(integrationExecutionData)
}


// All test
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
    executionData.setFrom(files(unitExecutionData, integrationExecutionData))
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)

    configureViolationRules(totalCoverageMinimum)

    classDirectories.setFrom(tasks.jacocoTestReport.get().classDirectories)
    executionData.setFrom(tasks.jacocoTestReport.get().executionData)
}

tasks.test {
    useJUnitPlatform()
    dependsOn(unitTest, integrationTest)
    finalizedBy("jacocoTestReport", "jacocoTestCoverageVerification")
}


tasks.build {
    dependsOn(
        "jacocoUnitTestCoverageVerification",
        "jacocoIntegrationTestCoverageVerification",
        "jacocoTestCoverageVerification",
    )
}
