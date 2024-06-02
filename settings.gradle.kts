pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ParticleView"
include(":sample:xml")
include(":sample:compose")
include(":particleview:core")
include(":particleview:xml")
include(":particleview:compose")
