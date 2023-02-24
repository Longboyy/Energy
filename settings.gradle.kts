rootProject.name = "energy"

pluginManagement {
	repositories {
		gradlePluginPortal()
		maven("https://repo.papermc.io/repository/maven-public/")
	}
}

include(":paper")
project(":paper").name = rootProject.name
