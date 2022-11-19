package de.siphalor.modpubup;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

/**
 * Configuration for the modpubup plugin
 */
public class ModPubUpExtension {
	private final Property<Boolean> requireExist;

	private final Property<Boolean> updateName;
	private final Property<Boolean> updateChangelog;
	private final Property<Boolean> updateGameVersions;
	private final Property<Boolean> updateReleaseType;
	private final Property<Boolean> updateLoaders;

	public ModPubUpExtension(ObjectFactory factory) {
		requireExist = factory.property(Boolean.class).convention(true);
		updateName = factory.property(Boolean.class).convention(true);
		updateChangelog = factory.property(Boolean.class).convention(false);
		updateGameVersions = factory.property(Boolean.class).convention(true);
		updateReleaseType = factory.property(Boolean.class).convention(true);
		updateLoaders = factory.property(Boolean.class).convention(false);
	}

	/**
	 * Sets if the task should fail if no matching version is found.
	 */
	public Property<Boolean> getRequireExist() {
		return requireExist;
	}

	/**
	 * Sets whether the version's name should be updated, enabled by default.
	 */
	public Property<Boolean> getUpdateName() {
		return updateName;
	}

	/**
	 * Sets whether the version's changelog should be updated, disabled by default.
	 */
	public Property<Boolean> getUpdateChangelog() {
		return updateChangelog;
	}

	/**
	 * Sets whether the version's supported Minecraft versions should be updated, enabled by default.
	 */
	public Property<Boolean> getUpdateGameVersions() {
		return updateGameVersions;
	}

	/**
	 * Sets whether the version's release type should be updated, enabled by default.
	 */
	public Property<Boolean> getUpdateReleaseType() {
		return updateReleaseType;
	}

	/**
	 * Sets whether the version's supported mod loaders should be updated, disabled by default.
	 */
	public Property<Boolean> getUpdateLoaders() {
		return updateLoaders;
	}
}
