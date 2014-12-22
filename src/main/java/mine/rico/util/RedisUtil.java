package mine.rico.util;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

	private static JedisPool pool = null;

	private static JedisPool getPool() {
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxActive(500);
			config.setMaxIdle(5);
			config.setMaxWait(1000 * 100);
			config.setTestOnBorrow(true);
			pool = new JedisPool(config, "192.168.16.237", 6381);
		}
		return pool;
	}

	public static void set(String key, String value) {
		System.out.println("set [" + key + "]");
		getPool().getResource().set(key, value);
	}

	public static String get(String key) {
		return getPool().getResource().get(key);
	}

	public static void delete(String key) {
		getPool().getResource().del(key);
	}

	public static void flush() {
		getPool().getResource().flushAll();
	}
}
