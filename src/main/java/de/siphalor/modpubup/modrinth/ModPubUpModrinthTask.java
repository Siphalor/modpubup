package de.siphalor.modpubup.modrinth;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.modrinth.minotaur.ModrinthExtension;
import de.siphalor.modpubup.ModPubUpExtension;
import de.siphalor.modpubup.util.Util;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.util.EntityUtils;
import org.apache.tools.ant.filters.StringInputStream;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class ModPubUpModrinthTask extends DefaultTask {
	private final Logger logger = Logging.getLogger(ModPubUpModrinthTask.class);
	private ModPubUpExtension modPubUpExtension;
	private ModrinthExtension modrinthExtension;

	public ModPubUpModrinthTask() {
		setGroup("upload");
	}

	@TaskAction
	public void run() {
		Project project = getProject();
		if (!project.getPluginManager().hasPlugin(Util.MINOTAUR_PLUGIN)) {
			return;
		}

		modPubUpExtension = project.getExtensions().getByType(ModPubUpExtension.class);

		modrinthExtension = project.getExtensions().getByType(ModrinthExtension.class);
		if (modrinthExtension.getVersionNumber().getOrNull() == null) {
			modrinthExtension.getVersionNumber().set(project.getVersion().toString());
		}

		try {
			String versionId = resolveVersionId();
			if (versionId == null) {
				String msg = "The version " + modrinthExtension.getVersionNumber() + " could not be found in the Modrinth project";
				if (modPubUpExtension.getRequireExist().get()) {
					throw new GradleException(msg);
				}
				logger.info(msg);
				return;
			}

			updateVersion(versionId);
		} catch (IOException e) {
			throw new GradleException("Failed to perform HTTP request", e);
		}
	}

	private String resolveVersionId() throws IOException {
		HttpClient httpClient = Util.getHttpClient();
		HttpResponse response = httpClient.execute(
				RequestBuilder.get(modrinthExtension.getApiUrl().get() + "/project/" + modrinthExtension.getProjectId().get() + "/version")
						.addHeader("Authorization", modrinthExtension.getToken().get())
						.build()
		);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new GradleException(Util.getHttpError("Failed to query Modrinth for version " + modrinthExtension.getVersionNumber(), response));
		}

		Gson gson = Util.getGson();
		JsonArray versions = gson.fromJson(EntityUtils.toString(response.getEntity()), JsonArray.class);
		for (JsonElement version : versions) {
			JsonObject versionObject = version.getAsJsonObject();
			if (modrinthExtension.getVersionNumber().get().equals(versionObject.get("version_number").getAsString())) {
				return versionObject.get("id").getAsString();
			}
		}
		return null;
	}

	private void updateVersion(String versionId) throws IOException {
		HttpClient httpClient = Util.getHttpClient();

		VersionPatch versionPatch = new VersionPatch();
		if (modPubUpExtension.getUpdateName().get()) {
			String versionName = modrinthExtension.getVersionName().getOrNull();
			if (versionName == null) {
				versionName = modrinthExtension.getVersionNumber().get();
			}
			versionPatch.name = versionName;
		}
		if (modPubUpExtension.getUpdateChangelog().get()) {
			versionPatch.changelog = modrinthExtension.getChangelog().get().replaceAll("\r\n", "\n");
		}
		if (modPubUpExtension.getUpdateGameVersions().get()) {
			if (modrinthExtension.getGameVersions().get().isEmpty()) {
				System.err.println("Skipping updating Modrinth game versions, since none were explicitly provided");
			} else {
				versionPatch.game_versions = modrinthExtension.getGameVersions().get();
			}
		}
		if (modPubUpExtension.getUpdateReleaseType().get()) {
			versionPatch.version_type = modrinthExtension.getVersionType().get().toLowerCase(Locale.ROOT);
		}
		if (modPubUpExtension.getUpdateLoaders().get()) {
			if (modrinthExtension.getLoaders().get().isEmpty()) {
				System.err.println("Skipping updating Modrinth loaders, since none were explicitly provided");
			} else {
				versionPatch.loaders = modrinthExtension.getLoaders().get();
			}
		}

		String json = Util.getGson().toJson(versionPatch);
		BasicHttpEntity entity = new BasicHttpEntity();
		entity.setContentLength(json.getBytes(StandardCharsets.UTF_8).length);
		entity.setContent(new StringInputStream(json));

		HttpResponse response = httpClient.execute(
				RequestBuilder.patch(modrinthExtension.getApiUrl().get() + "/version/" + versionId)
						.addHeader("Authorization", modrinthExtension.getToken().get())
						.addHeader("Content-Type", "application/json")
						.setEntity(entity)
						.build()
		);
		StatusLine status = response.getStatusLine();
		if (status.getStatusCode() != 204) {
			throw new GradleException(Util.getHttpError("Failed to update Modrinth version " + versionId, response));
		}
		logger.info("Successfully updated Modrinth version " + versionId + " (" + modrinthExtension.getVersionNumber() + ")");
	}
}
