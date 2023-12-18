plugins {
    java
    kotlin("jvm") version "1.9.21"
    `maven-publish`
    id("org.jetbrains.dokka") version "1.9.10"
}

group = "dev.retrotv"
version = "0.1.0-alpha"

// Github Action 버전 출력용
tasks.register("printVersionName") {
    println(project.version)
}

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    implementation("com.github.retrotv-maven-repo:cryptography:0.21.0-alpha")
    implementation("com.github.retrotv-maven-repo:data-utils:0.13.2-alpha")

    implementation("org.apache.tika:tika-core:2.9.1")
    implementation("org.apache.tika:tika-parsers:2.9.1")

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