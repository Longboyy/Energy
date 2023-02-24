plugins {
	`java-library`
	id("io.papermc.paperweight.userdev") version "1.3.1"
}

dependencies {
	paperDevBundle("1.18.1-R0.1-SNAPSHOT")

	compileOnly("net.civmc.banstick:BanStick:2.1.0:dev-all")
	compileOnly("net.civmc.civmodcore:CivModCore:2.4.1:dev-all")
	compileOnly("net.civmc.namelayer:NameLayer:3.1.0:dev")
	compileOnly("com.github.NuVotifier.NuVotifier:nuvotifier-bukkit:2.6.0")
}
