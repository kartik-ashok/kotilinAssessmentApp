pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
<<<<<<< HEAD
//        jcenter() //here
=======
>>>>>>> 3f36c4f7dd7f47ced76ba2aa04eaaaeea6777ad9
        mavenCentral()
    }
}

<<<<<<< HEAD
rootProject.name = "crud4"
=======
rootProject.name = "news app"
>>>>>>> 3f36c4f7dd7f47ced76ba2aa04eaaaeea6777ad9
include(":app")
 