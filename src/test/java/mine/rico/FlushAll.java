package mine.rico;

import mine.rico.util.RedisUtil;

public class FlushAll {

	public static void main(String[] args) {
		RedisUtil.flushAll();
		System.out.println("flush all");
	}
}
