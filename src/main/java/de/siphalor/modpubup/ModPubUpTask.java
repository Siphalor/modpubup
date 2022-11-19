package de.siphalor.modpubup;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class ModPubUpTask extends DefaultTask {
	public ModPubUpTask() {
		setGroup("upload");
	}

	@TaskAction
	public void run() {
	}
}
