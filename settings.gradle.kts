pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven("https://s01.oss.sonatype.org/content/groups/public")
        maven("https://maven.aliyun.com/repository/public")
        maven("https://jitpack.io")
    }
}

rootProject.name = "CommonLib"
include(":app")
include("common")
