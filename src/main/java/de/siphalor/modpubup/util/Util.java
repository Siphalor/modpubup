package de.siphalor.modpubup.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Util {
	public static final String MINOTAUR_PLUGIN = "com.modrinth.minotaur";
	public static final String CURSEGRADLE_PLUGIN = "com.matthewprenger.cursegradle";

	private static HttpClient httpClient;

	public static HttpClient getHttpClient() {
		if (httpClient == null) {
			httpClient = HttpClientBuilder.create()
					.setUserAgent("de/siphalor/modpubup (ModPubUp Gradle plugin - dev@siphalor.de)")
					.build();
		}
		return httpClient;
	}

	private static Gson gson;

	public static Gson getGson() {
		if (gson == null) {
			gson = new GsonBuilder().disableHtmlEscaping().create();
		}
		return gson;
	}

	public static String getHttpError(String error, HttpResponse response) {
		error += ", got " + response.getStatusLine();
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			try {
				error += ": " + EntityUtils.toString(entity);
			} catch (IOException ignored) {
			}
		}
		return error;
	}
}
