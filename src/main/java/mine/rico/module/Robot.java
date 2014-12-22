package mine.rico.module;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import mine.rico.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class Robot {

	private static final String URL = "http://www.simsimi.com/func/reqN?fl=http%3A%2F%2Fwww.simsimi.com%2Ftalk.htm&lc=ch&ft=0.0&req=";

	private static final int REPEAT_TIME = 3;

	@SuppressWarnings("serial")
	private static final Map<String, String> PROPERTIES = new HashMap<String, String>() {

		{
			put("Cookie", "simsimi_uid=51678750; Filtering=0.0; sid=s%3ArYalXpIcOh3zAEo0SDWep7TQ.SjEbAoTeYsXMLm9TgzyIGHpge1yrSBzNbcj8Cvvp87A; AWSELB=150F676708F2639057F41EA6CD9115064C58E864E4D5FE3F62AF683EB3CA54C1A44837308BAB86F4F48D2BA2A2B01B0AEA34FBA3D92BA7AB89083051C189504CF5589F0BF7; sim_name=%u5BA2%u4EBA; teach_btn_url=talk; selected_nc=ch;");
			put("Host", "www.simsimi.com");
			put("Referer", "http://www.simsimi.com/talk.htm");
		}
	};

	public static synchronized String chat(String text) {
		try {
			return chat(URL + URLEncoder.encode(text, HttpUtil.DEFAULT_CHARSET), REPEAT_TIME);
		} catch (UnsupportedEncodingException e) {
			return "我无法理解你！";
		}
	}

	private static String chat(String url, int repeatTime) {
		if (repeatTime-- == 1) {
			return "你说什么？敢再说一遍吗？";
		}
		String response = null;
		try {
			response = new JSONObject(HttpUtil.doGet(url, PROPERTIES)).getString("sentence_resp");
		} catch (IOException e) {
			return chat(url, repeatTime);
		} catch (JSONException e) {
			return "不清楚你在说啥.....";
		}
		return response;
	}
}
