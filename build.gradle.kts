import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI

plugins {
    java
    jacoco
    `maven-publish`
    kotlin("jvm") version "2.1.21"
    id("com.vanniktech.maven.publish") version "0.32.0"
    id("org.jetbrains.dokka") version "2.0.0"
    id("org.sonarqube") version "4.0.0.2929"
}

group = "dev.retrotv"
version = "1.4.3"

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

// Github Action 버전 출력용
tasks.register("printVersionName") {
    description = "이 프로젝트의 버전을 출력합니다."
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    println(project.version)
}

repositories {
    mavenCentral()
}

val cryptography = "0.51.1-alpha"
val dataUtils = "0.23.3-alpha"
val tika = "2.9.4" // tika 3.0.0 부터 java 11을 요구하므로 바꾸지 말 것
val poi = "5.4.1"
val junit = "5.13.4"
val slf4j = "2.0.17"
val log4j = "2.25.1"

dependencies {
    configurations.all {
        exclude(group = "com.fasterxml.jackson.module", module = "jackson-module-kotlin")
    }

    implementation("dev.retrotv:cryptography-core:${cryptography}")
    implementation("dev.retrotv:cryptography-hash:${cryptography}")
    implementation("dev.retrotv:data-utils:${dataUtils}")

    // Apache Tika
    implementation("org.apache.tika:tika-core:${tika}")
    implementation("org.apache.tika:tika-parsers-standard-package:${tika}")

    // Apache POI
    implementation("org.apache.poi:poi-ooxml:${poi}")
    implementation("org.apache.poi:poi:${poi}")

    // Logger
    compileOnly("org.slf4j:slf4j-api:${slf4j}")
    testImplementation("org.slf4j:slf4j-api:${slf4j}")
    testImplementation("org.apache.logging.log4j:log4j-core:${log4j}")
    testImplementation("org.apache.logging.log4j:log4j-slf4j2-impl:${log4j}")

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

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), project.name, version.toString())

    pom {
        name.set("extended-file")
        description.set("Java의 File 클래스를 확장한 라이브러리 입니다.")
        inceptionYear.set("2025")
        url.set("https://github.com/retrotv-maven-repo/extended-file")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("yjj8353")
                name.set("JaeJun Yang")
                email.set("yjj8353@gmail.com")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/retrotv-maven-repo/extended-file.git")
            developerConnection.set("scm:git:ssh://github.com/retrotv-maven-repo/extended-file.git")
            url.set("https://github.com/retrotv-maven-repo/extended-file.git")
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
    }
}

tasks.withType<Sign>().configureEach {
    onlyIf {

        // 로컬 및 깃허브 패키지 배포 시에는 서명하지 않도록 설정
        !gradle.taskGraph.hasTask(":publishMavenPublicationToMavenLocal") && !gradle.taskGraph.hasTask(":publishMavenPublicationToGitHubPackagesRepository")
    }
}

kotlin {
    jvmToolchain(8)
}

apply(from = "${rootDir}/gradle/sonarcloud.gradle")
apply(from = "${rootDir}/gradle/jacoco.gradle")
