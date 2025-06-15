import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jreleaser.gradle.plugin.JReleaserExtension
import org.jreleaser.model.Active
import java.time.LocalDate

plugins {
    java
    jacoco
    `maven-publish`
    id("org.jreleaser") version "1.18.0"
    kotlin("jvm") version "2.1.21"
    id("org.jetbrains.dokka") version "2.0.0"
    id("org.sonarqube") version "4.0.0.2929"
}

group = "dev.retrotv"
version = "1.2.2"

// Github Action 버전 출력용
tasks.register("printVersionName") {
    description = "이 프로젝트의 버전을 출력합니다."
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    println(project.version)
}

java {
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

val cryptography = "0.47.0-alpha"
val dataUtils = "0.21.6-alpha"
val tika = "2.9.2" // tika 3.0.0 부터 java 11을 요구하므로 바꾸지 말 것
val poi = "5.4.0"
val junit = "5.11.4"
val slf4j = "2.0.16"
val log4j = "2.24.3"

dependencies {
    implementation("com.github.retrotv-maven-repo:cryptography:${cryptography}")
    implementation("com.github.retrotv-maven-repo:data-utils:${dataUtils}")

    implementation("org.apache.tika:tika-core:${tika}")
    implementation("org.apache.tika:tika-parsers:${tika}")

    implementation("org.apache.poi:poi-ooxml:${poi}")
    implementation("org.apache.poi:poi:${poi}")

    // Logger
    implementation("org.slf4j:slf4j-api:${slf4j}")
    implementation("org.apache.logging.log4j:log4j-core:${log4j}")
    implementation("org.apache.logging.log4j:log4j-api:${log4j}")

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

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

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

            repositories {
                maven {
                    url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
                }
            }
        }
    }
}

configure<JReleaserExtension> {
    gitRootSearch = true
    project {
        description = "Java의 File 클래스를 확장한 라이브러리 입니다."
        authors = listOf("yjj8353", "retrotv-maven-repo")
        license = "The Apache License, Version 2.0"
        links {
            homepage = "https://github.com/retrotv-maven-repo/extended-file"
            bugTracker = "https://github.com/retrotv-maven-repo/extended-file/issues"
            contact = "https://github.com/retrotv-maven-repo"
        }
        inceptionYear = "2025"
        vendor = "retrotv-maven-repo"
        copyright = "Copyright (c) ${LocalDate.now().year} Your Organization"
    }

    release {
        github {
            commitAuthor {
                name = "retrotv-maven-repo"
                email = "yjj8353@gmail.com"
            }
        }
    }

    signing {
        active = Active.ALWAYS
        armored = true
    }

    deploy {
        maven {
            mavenCentral {
                register("sonatype") {
                    active = Active.ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
//                    subprojects.filter { it.name != "examples" }.forEach { project ->
//                        stagingRepository(project.layout.buildDirectory.dir("staging-deploy").get().asFile.path)
//                    }
                }
            }
        }
    }
}

//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//            from(components["java"])
//            groupId = project.group.toString()
//            artifactId = project.name
//            version = project.version.toString()
//
//            pom {
//                name.set("extended-file")
//                description.set("Java의 File 클래스를 확장한 라이브러리 입니다.")
//                inceptionYear.set("2025")
//                url.set("https://github.com/retrotv-maven-repo/extended-file")
//
//                licenses {
//                    license {
//                        name.set("The Apache License, Version 2.0")
//                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
//                    }
//                }
//
//                developers {
//                    developer {
//                        id.set("yjj8353")
//                        name.set("JaeJun Yang")
//                        email.set("yjj8353@gmail.com")
//                    }
//                }
//
//                scm {
//                    connection.set("scm:git:git://github.com/retrotv-maven-repo/extended-file.git")
//                    developerConnection.set("scm:git:ssh://github.com/retrotv-maven-repo/extended-file.git")
//                    url.set("https://github.com/retrotv-maven-repo/extended-file.git")
//                }
//            }
//        }
//    }
//}

//publishing {
//    repositories {
//
//        // Github Packages에 배포하기 위한 설정
//        maven {
//            name = "GitHubPackages"
//            url = URI("https://maven.pkg.github.com/retrotv-maven-repo/extended-file")
//            credentials {
//                username = System.getenv("USERNAME")
//                password = System.getenv("PASSWORD")
//            }
//        }
//    }
//
//    publications {
//        create<MavenPublication>("maven") {
//            groupId = project.group.toString()
//            artifactId = project.name
//            version = project.version.toString()
//            from(components["java"])
//        }
//    }
//}

kotlin {
    jvmToolchain(8)
}

apply(from = "${rootDir}/gradle/sonarcloud.gradle")
apply(from = "${rootDir}/gradle/jacoco.gradle")