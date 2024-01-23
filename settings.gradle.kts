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
        maven {
            url = run {
                val dependencyText = providers.exec {
                    workingDir = rootDir
                    commandLine("cargo", "metadata", "--format-version", "1")
                }.standardOutput.asText.get()

                val dependencyJson = groovy.json.JsonSlurper().parseText(dependencyText) as Map<String, Object>
                val pkgMeta = (dependencyJson["packages"] as List<Object>).find { (it as Map<String, String>)["name"] == "rustls-platform-verifier-android" }
                val manifestPath = file((pkgMeta as Map<String, String>)["manifest_path"] as Any)
                File(manifestPath.parentFile, "maven").toURI()
            }
            metadataSources.artifact()
        }
    }
}

rootProject.name = "jnidemo"
include(":app")
