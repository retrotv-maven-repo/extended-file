import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI

plugins {
    java
    jacoco
    `maven-publish`
    kotlin("jvm") version "2.0.21"
    id("org.jetbrains.dokka") version "2.0.0"
    id("org.sonarqube") version "4.0.0.2929"
}

group = "dev.retrotv"
version = "1.1.4"

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

val cryptography = "0.47.2-alpha"
val dataUtils = "0.21.6-alpha"
val tika = "2.9.2" // tika 3.0.0 부터 java 11을 요구하므로 바꾸지 말 것
val poi = "5.3.0"
val junit = "5.11.4"

dependencies {
    implementation("com.github.retrotv-maven-repo:cryptography:${cryptography}")
    implementation("com.github.retrotv-maven-repo:data-utils:${dataUtils}")

    implementation("org.apache.tika:tika-core:${tika}")
    implementation("org.apache.tika:tika-parsers:${tika}")

    implementation("org.apache.poi:poi-ooxml:${poi}")
    implementation("org.apache.poi:poi:${poi}")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:${junit}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junit}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${junit}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${junit}")
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
    repositories {

        // Github Packages에 배포하기 위한 설정
        maven {
            name = "GitHubPackages"
            url = URI("https://maven.pkg.github.com/retrotv-maven-repo/extended-file")
            credentials {
                username = System.getenv("USERNAME")
                password = System.getenv("PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(components["java"])
        }
    }
}

kotlin {
    jvmToolchain(8)
}

apply(from = "${rootDir}/gradle/sonarcloud.gradle")
apply(from = "${rootDir}/gradle/jacoco.gradle")
