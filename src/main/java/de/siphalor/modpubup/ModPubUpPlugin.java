package de.siphalor.modpubup;

import de.siphalor.modpubup.modrinth.ModPubUpModrinthTask;
import de.siphalor.modpubup.util.Util;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ModPubUpPlugin implements Plugin<Project> {
	@Override
	public void apply(Project target) {
		target.getExtensions().create("modpubup", ModPubUpExtension.class, target.getObjects());

		ModPubUpTask mainTask = target.getTasks().create("modpubup", ModPubUpTask.class);
		if (target.getPlugins().hasPlugin(Util.MINOTAUR_PLUGIN)) {
			ModPubUpModrinthTask modrinthTask = target.getTasks().create("modpubupModrinth", ModPubUpModrinthTask.class);
			mainTask.finalizedBy(modrinthTask);
		}
	}
}
