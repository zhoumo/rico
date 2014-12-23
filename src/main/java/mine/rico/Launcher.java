package mine.rico;

import mine.rico.module.TopicListener;
import mine.rico.util.ConfigUtil;
import mine.rico.util.RedisUtil;

public class Launcher {

	public static void main(String[] args) throws InterruptedException {
		final Server server = new Server();
		server.login();
		boolean isSub = false;
		while (true) {
			Thread.sleep(10000);
			for (final String publishKey : ConfigUtil.get(ConfigUtil.PUBLISH_KEYS).split(",")) {
				if (!Server.RUNNING || !RedisUtil.exists(publishKey)) {
					continue;
				}
				for (String key : RedisUtil.hGet(publishKey).keySet()) {
					if (RedisUtil.exists(key) && !isSub) {
						new Thread(new Runnable() {

							public void run() {
								RedisUtil.subscribe(publishKey, new TopicListener(server));
							}
						}).start();
						isSub = true;
					}
				}
			}
		}
	}
}
