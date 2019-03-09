package jp.azisaba.main.homogui.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class PlayerSkinUtils {

	public static String getSkinFromPlayerName(String playerName) {
		String uuidStr = getUUID(playerName);
		return getSkinFromUUID(uuidStr);
	}

	public static String getSkinFromUUID(String uuid) {
		String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid;

		List<String> list = getPageList(url);

		if (list.size() <= 0) {
			return null;
		}

		String data = list.get(0);
		String textureFirst = "\"properties\":[{\"name\":\"textures\",\"value\":\"";

		String texture = data.substring(data.indexOf(textureFirst) + textureFirst.length());
		texture = texture.substring(0, texture.indexOf("\"}"));

		return texture;
	}

	public static String getUUID(String playerName) {
		List<String> list = getPageList("https://api.mojang.com/users/profiles/minecraft/" + playerName);

		for (String s : list) {
			if (s.startsWith("{\"id\":")) {

				String first = "{\"id\":\"";
				String uuidStr = s.substring(first.length(), s.indexOf(",") - 1);
				return uuidStr;
			}
		}

		return null;
	}

	private static List<String> getPageList(String url) {
		try {
			String charset = "UTF-8";
			List<String> contents = read(url, charset);
			return contents;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static List<String> read(String url, String charset) throws Exception {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			URLConnection conn = new URL(url).openConnection();
			is = conn.getInputStream();
			isr = new InputStreamReader(is, charset);
			br = new BufferedReader(isr);

			ArrayList<String> lineList = new ArrayList<String>();
			String line = null;
			while ((line = br.readLine()) != null) {
				lineList.add(line);
			}
			return lineList;
		} finally {
			try {
				br.close();
			} catch (Exception e) {
			}
			try {
				isr.close();
			} catch (Exception e) {
			}
			try {
				is.close();
			} catch (Exception e) {
			}
		}
	}
}
