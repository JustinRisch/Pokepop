package resources;

import java.util.List;

public class HipchatProp {
	private static String roomName, baseUrl, apiKey;
	/*
	 * In the resource folder of this project, you must include a
	 * "Hipchat.properties" which contains, on separate lines:
	 1. Room Name
	 2. API Key
	 3. BaseURL
	 */
	static {
		List<String> lines = ResourceLoader.getResource("Hipchat.properties");
		roomName = lines.get(0);
		baseUrl = lines.get(1);
		apiKey = lines.get(2);
	}

	public static String getApiKey() {
		return apiKey;
	}

	public static String getBaseUrl() {
		return baseUrl;
	}

	public static String getRoomName() {
		return roomName;
	}

}
