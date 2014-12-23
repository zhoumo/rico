package mine.rico.module;

import iqq.im.bean.QQMsg;
import mine.rico.util.RedisUtil;
import mine.rico.util.StringUtil;
import redis.clients.jedis.JedisPubSub;

public class Listener extends JedisPubSub {

	private Server server;

	public Listener(Server server) {
		this.server = server;
	}

	@Override
	public void onMessage(String channel, String message) {
		if (RedisUtil.exists(channel)) {
			for (String uin : RedisUtil.hGet(channel).keySet()) {
				System.out.println(channel + "-" + uin + "-" + message);
				server.sendMsg((QQMsg) StringUtil.Deserialize(RedisUtil.get(channel + uin)), message);
			}
		}
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
	}

	@Override
	public void onPSubscribe(String channel, int subscribedChannels) {
	}

	@Override
	public void onPUnsubscribe(String channel, int subscribedChannels) {
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
	}
}
