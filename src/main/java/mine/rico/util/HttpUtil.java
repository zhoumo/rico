package mine.rico.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {

	public static final String DEFAULT_CHARSET = "UTF-8";

	public static String doGet(String url, Map<String, String> properties) throws IOException {
		HttpURLConnection conn = getConnection(new URL(url), "GET");
		if (properties != null && !properties.isEmpty()) {
			for (Entry<String, String> p : properties.entrySet()) {
				conn.setRequestProperty(p.getKey(), p.getValue());
			}
		}
		String response = getStreamAsString(conn.getInputStream());
		conn.disconnect();
		return response;
	}

	private static HttpURLConnection getConnection(URL url, String method) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setConnectTimeout(3000);
		conn.setReadTimeout(3000);
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.17 Safari/537.36");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + DEFAULT_CHARSET);
		conn.setRequestProperty("Connection", "Keep-Alive");
		return conn;
	}

	private static String getStreamAsString(InputStream input) throws IOException {
		StringBuilder builder = new StringBuilder(100);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input, DEFAULT_CHARSET));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			return builder.toString();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
}
