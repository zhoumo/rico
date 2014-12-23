package mine.rico;

import mine.rico.util.RedisUtil;

public class Publish {

	public static void main(String[] args) {
		RedisUtil.publish("test", "ok");
	}
}
