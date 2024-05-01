plugins {
    java
    kotlin("jvm") version "1.9.23"
    `maven-publish`
    id("org.jetbrains.dokka") version "1.9.20"
}

group = "dev.retrotv"
version = "0.2.1-alpha"

// Github Action 버전 출력용
tasks.register("printVersionName") {
    println(project.version)
}

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

val tika = "2.9.2"
val poi = "5.2.5"

dependencies {
    implementation("com.github.retrotv-maven-repo:cryptography:0.23.0-alpha")
    implementation("com.github.retrotv-maven-repo:data-utils:0.16.0-alpha")

    implementation("org.apache.tika:tika-core:${tika}")
    implementation("org.apache.tika:tika-parsers:${tika}")
    implementation("org.apache.tika:tika-parsers-standard-package:${tika}")

    implementation("org.apache.poi:poi-ooxml:${poi}")
    implementation("org.apache.poi:poi:${poi}")

    testImplementation(kotlin("test"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
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
}

kotlin {
    jvmToolchain(8)
}