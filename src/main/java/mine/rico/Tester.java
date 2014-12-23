package mine.rico;

import mine.rico.util.RedisUtil;

public class Tester {

	public static void main(String[] args) {
		System.out.println(RedisUtil.hGet("订阅"));
	}
}
