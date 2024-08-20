import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    jacoco
    kotlin("jvm") version "2.0.10"
    `maven-publish`
    id("org.jetbrains.dokka") version "1.9.20"
    id("org.sonarqube") version "4.0.0.2929"
}

jacoco {
    toolVersion = "0.8.12"
}

group = "dev.retrotv"
version = "0.5.1-alpha"

// Github Action 버전 출력용
tasks.register("printVersionName") {
    description = "이 프로젝트의 버전을 출력합니다."
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    println(project.version)
}

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

val cryptography = "0.30.0-alpha"
val dataUtils = "0.16.0-alpha"
val tika = "2.9.2"
val poi = "5.2.5"

dependencies {
    implementation("com.github.retrotv-maven-repo:cryptography:${cryptography}")
    implementation("com.github.retrotv-maven-repo:data-utils:${dataUtils}")

    implementation("org.apache.tika:tika-core:${tika}")
    implementation("org.apache.tika:tika-parsers:${tika}")
    implementation("org.apache.tika:tika-parsers-standard-package:${tika}")

    implementation("org.apache.poi:poi-ooxml:${poi}")
    implementation("org.apache.poi:poi:${poi}")

    testImplementation(kotlin("test"))
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
    }
    compileTestKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "file"
            version = project.version.toString()

            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

kotlin {
    jvmToolchain(8)
}

tasks.jacocoTestReport {
    reports {

        // HTML 파일을 생성하도록 설정
        html.required = true

        // SonarQube에서 Jacoco XML 파일을 읽을 수 있도록 설정
        xml.required = true
        csv.required = false
    }
}

sonar {
    properties {
        property("sonar.projectKey", "retrotv-maven-repo_extended-file")
        property("sonar.organization", "retrotv-maven-repo")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
