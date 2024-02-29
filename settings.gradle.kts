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

rootProject.name = "NoteApp"
include(":app")
include(":benchmark")
include(":core:data")
include(":core:domain")
include(":core:work")
include(":features:notes")
include(":features:add-edit-note")
include(":shared-test")
include(":core:core")
include(":core:designsystem")
