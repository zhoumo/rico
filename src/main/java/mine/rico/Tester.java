package mine.rico;

import mine.rico.util.RedisUtil;

public class Tester {

	public static void main(String[] args) {
		// RedisUtil.flushAll();
		RedisUtil.publish("test", "ok");
	}
}
