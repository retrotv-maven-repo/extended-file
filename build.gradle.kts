// import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI

plugins {
    java
    jacoco
    // signing
    `maven-publish`
    kotlin("jvm") version "2.0.21"
    // id("com.vanniktech.maven.publish") version "0.30.0"
    id("org.jetbrains.dokka") version "1.9.20"
    id("org.sonarqube") version "4.0.0.2929"
}

group = "dev.retrotv"
version = "1.1.1"

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

val cryptography = "0.44.0-alpha"
val dataUtils = "0.21.6-alpha"
val tika = "2.9.2" // tika 3.0.0 부터 java 11을 요구하므로 바꾸지 말 것
val poi = "5.3.0"

dependencies {
    implementation("com.github.retrotv-maven-repo:cryptography:${cryptography}")
    implementation("com.github.retrotv-maven-repo:data-utils:${dataUtils}")

    implementation("org.apache.tika:tika-core:${tika}")
    implementation("org.apache.tika:tika-parsers:${tika}")

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
    repositories {

        maven {
            name = "MavenCentral"
            url = URI("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
        }

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

/*mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    coordinates("dev.retrotv", "extended-file", project.version.toString())

    pom {
        name.set("Extended File")
        description.set("File 클래스의 기능을 확장한 클래스입니다.")
        url.set("https://github.com/retrotv-maven-repo/extended-file")
        licenses {
            license {
                name.set("Apache License 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("yjj8353")
                name.set("yjj8353")
                email.set("yjj8353@gmail.com")
                url.set("https://blog.retrotv.dev")
            }
        }
        scm {
            connection.set("scm:git:github.com/retrotv-maven-repo/extended-file.git")
            developerConnection.set("scm:git:ssh://github.com/retrotv-maven-repo/extended-file.git")
            url.set("https://github.com/retrotv-maven-repo/extended-file/tree/deploy")
        }
    }
}

signing {
    sign(publishing.publications)
}*/

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
