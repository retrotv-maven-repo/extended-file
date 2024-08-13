import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    jacoco
    kotlin("jvm") version "2.0.0"
    `maven-publish`
    id("org.jetbrains.dokka") version "1.9.20"
}

jacoco {
    toolVersion = "0.8.12"
}

group = "dev.retrotv"
version = "0.2.1-alpha"

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

val cryptography = "0.23.0-alpha"
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
        html.required = true
        xml.required = false
        csv.required = false
    }

    finalizedBy("jacocoTestCoverageVerification")
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {

            // 'element'가 없으면 프로젝트의 전체 파일을 합친 값을 기준으로 한다.
            limit {

                // 'counter'를 지정하지 않으면 default는 'INSTRUCTION'
                // 'value'를 지정하지 않으면 default는 'COVEREDRATIO'
                minimum = "0.80".toBigDecimal()
            }
        }

        rule {

            // 룰을 간단히 켜고 끌 수 있다.
            enabled = true

            // 룰을 체크할 단위는 클래스 단위
            element = "CLASS"

            // 브랜치 커버리지를 최소한 90% 만족시켜야 한다.
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.90".toBigDecimal()
            }

            // 라인 커버리지를 최소한 80% 만족시켜야 한다.
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.80".toBigDecimal()
            }

            // 빈 줄을 제외한 코드의 라인수를 최대 200라인으로 제한한다.
            limit {
                counter = "LINE"
                value = "TOTALCOUNT"
                maximum = "200".toBigDecimal()
            }

            // 커버리지 체크를 제외할 클래스들
            excludes = listOf(
//                    "*.test.*",
//                    "*.Kotlin*"
            )
        }
    }
}

val testCoverage by tasks.registering {
    group = "verification"
    description = "Runs the unit tests with coverage"

    dependsOn(":test",
        ":jacocoTestReport",
        ":jacocoTestCoverageVerification")

    tasks["jacocoTestReport"].mustRunAfter(tasks["test"])
    tasks["jacocoTestCoverageVerification"].mustRunAfter(tasks["jacocoTestReport"])
}
