pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    // 호환성 테스트를 제외한 빌드에서만 적용
    val isCompatibilityTest = System.getProperty("compatibilityTest")?.toBoolean() ?: false
    if (!isCompatibilityTest) {
        plugins {
            id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
        }
    }
}

rootProject.name = "extended-file"
