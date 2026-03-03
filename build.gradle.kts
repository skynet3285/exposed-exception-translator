plugins {
    kotlin("jvm") version "2.3.0"
    `java-library`
    `maven-publish`
    id("org.springframework.boot") version "4.0.3" apply false
    id("io.spring.dependency-management") version "1.1.7"

    // kotlin lint
    id("org.jlleitschuh.gradle.ktlint") version "14.0.1"
    kotlin("plugin.spring") version "2.3.0"
}

apply(plugin = "io.spring.dependency-management")

ktlint {
    version.set("1.8.0")
}

group = "com.github.skynet3285"
version = "1.0.0"

repositories {
    mavenCentral()
}

val springBootVersion by extra("4.0.3")
val mockkVersion by extra("1.14.9")
val kotestVersion by extra("6.1.3")
val exposedVersion by extra("1.0.0")

dependencies {
    implementation(kotlin("stdlib"))

    compileOnly("org.springframework.boot:spring-boot-starter-webmvc:$springBootVersion")
    compileOnly("org.springframework.boot:spring-boot-starter-data-jdbc:$springBootVersion")
    compileOnly("org.jetbrains.exposed:exposed-core:$exposedVersion")
    compileOnly("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jdbc:$springBootVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.jetbrains.exposed:exposed-spring-boot4-starter:$exposedVersion")
    testImplementation("io.kotest:kotest-runner-junit6:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-extensions-spring:$kotestVersion")

    // For Temporary H2 Database Testing
    testImplementation("com.zaxxer:HikariCP:7.0.2")
    testRuntimeOnly("com.h2database:h2:2.4.240")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain(21)
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.jar {
    enabled = true
    archiveClassifier.set("")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
    // https://kotest.io/docs/6.0/framework/project-config.html#setup
    jvmArgs("-XX:+EnableDynamicAgentLoading")
    systemProperty("kotest.framework.config.fqn", "com.github.skynet3285.exposed.exception.translator.config.TestConfig")
}
