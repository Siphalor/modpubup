plugins {
	id("java-gradle-plugin")
    id("com.gradle.plugin-publish").version("1.1.0")
}

group = "de.siphalor"
version = "0.1.0"

repositories {
    mavenCentral()
	gradlePluginPortal()
}

dependencies {
	api("org.apache.httpcomponents:httpclient:4.5.13")

	compileOnly("com.modrinth.minotaur:com.modrinth.minotaur.gradle.plugin:2.4.4")
	compileOnly("gradle.plugin.com.matthewprenger:CurseGradle:1.4.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

java {
	withSourcesJar()
	withJavadocJar()
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

gradlePlugin {
	plugins {
		create("modpubup") {
			id = "${project.group}.modpubup"
			implementationClass = "${project.group}.modpubup.ModPubUpPlugin"
		}
	}
}

pluginBundle {
	description = "Plugin for updating the metadata of published Minecraft mod versions"
	tags = listOf("minecraft", "mods", "modrinth", "curseforge")
}

publishing {
	repositories {
		if (project.hasProperty("siphalorMavenUser")) {
			maven {
				name = "Siphalor"
				url = uri("https://maven.siphalor.de/upload.php")
				credentials {
					username = project.property("siphalorMavenUser").toString()
					password = project.property("siphalorMavenPassword").toString()
				}
			}
		}
	}
}
