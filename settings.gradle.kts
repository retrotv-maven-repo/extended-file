pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    // compatibilityTest 시스템 속성이 true로 설정된 경우 Foojay 플러그인을 적용하지 않음
    val isCompatibilityTest = System.getProperty("compatibilityTest")?.toBoolean() ?: false
    if (!isCompatibilityTest) {
        plugins {
            id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
        }
    }
}

rootProject.name = "extended-file"
