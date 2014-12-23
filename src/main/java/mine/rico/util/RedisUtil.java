package mine.rico.util;

import java.util.Map;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

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

	public static boolean exists(String key) {
		return getPool().getResource().exists(key);
	}

	public static void flushAll() {
		getPool().getResource().flushAll();
	}

	public static void set(String key, String value) {
		getPool().getResource().set(key, value);
	}

	public static String get(String key) {
		return getPool().getResource().get(key);
	}

	public static void del(String key) {
		getPool().getResource().del(key);
	}

	public static void hSet(String key, String field) {
		getPool().getResource().hset(key, field, "");
	}

	public static Map<String, String> hGet(String key) {
		return getPool().getResource().hgetAll(key);
	}

	public static void hDel(String key, String field) {
		getPool().getResource().hdel(key, field);
	}

	public static void subscribe(String channel, JedisPubSub jedisPubSub) {
		getPool().getResource().subscribe(jedisPubSub, channel);
	}

	public static void publish(String channel, String message) {
		getPool().getResource().publish(channel, message);
	}
}
