package mine.rico.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

	public static final String QQ_NUMBER = "QQ_NUMBER";

	public static final String QQ_PASSWORD = "QQ_PASSWORD";

	public static final String PUBLISH_KEYS = "PUBLISH_KEYS";

	private static Properties config = null;

	private static Properties getConfig() {
		if (config == null) {
			config = new Properties();
			InputStream in = ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties");
			try {
				config.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return config;
	}

	public static String get(String key) {
		try {
			String value = getConfig().getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
