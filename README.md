<div align="center">

# ModPubUp

[![latest maven release](https://img.shields.io/maven-metadata/v?color=0f9fbc&metadataUrl=https%3A%2F%2Fmaven.siphalor.de%2Fde%2Fsiphalor%2Fmodpubup%2Fmaven-metadata.xml&style=flat-square)](https://maven.siphalor.de/de/siphalor/modpubup/)

Easily update the metadata of published Minecraft mod files using Gradle

</div>


## Usage

Include the plugin's repository in your `settings.gradle(.kts)`:

```groovy
pluginManagement {
	repositories {
		// Other plugin repositories
		maven {
			name = "Siphalor"
			url = "https://maven.siphalor.de/"
		}
	}
}
```

Use it in your `build.gradle(.kts)`:

```groovy
plugins {
	// Other plugins
	id "de.siphalor.modpubup" version "0.1.0" // Check the latest version at the top of the readme
}
```

This plugin adds the following tasks:

- `modpubup`: Performs all the below tasks
- `modpubupModrinth` (in the presence of [Minotaur](https://github.com/modrinth/minotaur)): Update the latest Modrinth publication
- `modpubupCurseforge` (*to be done*; in the presence of [CurseGradle](https://github.com/matthewprenger/CurseGradle)): Update the latest CurseForge publications
- `modpubupCurseforgeXXXX` (*to be done*): Same as the above, but only for the project with the id `XXXX`

## Configuration

The plugin offers some basic configuration options, shown here with their defaults:

```groovy
modpubup {
	requireExist       = true  // Fails if the latest publication can not be found
	updateName         = true  // Update the version name
	updateChangelog    = false // Update the changelog
	updateGameVersions = true  // Update the supported game versions
	updateReleaseType  = true  // Update the release type
	updateLoaders      = false // Update the supported loaders
}
```

The supported game versions and mod loaders will not be automatically detected and must be specified explicitly in the configuration sections of Minotaur/CurseGradle.
