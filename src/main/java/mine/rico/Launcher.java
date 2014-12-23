package mine.rico;

import mine.rico.module.Listener;
import mine.rico.module.Server;
import mine.rico.util.ConfigUtil;
import mine.rico.util.RedisUtil;

public class Launcher {

	public static void main(String[] args) throws InterruptedException {
		final Server server = new Server();
		server.login();
		while (true) {
			Thread.sleep(10000);
			for (final String publishKey : ConfigUtil.get(ConfigUtil.PUBLISH_KEYS).split(",")) {
				if (!Server.RUNNING || !RedisUtil.exists(publishKey)) {
					continue;
				}
				new Thread(new Runnable() {

					public void run() {
						RedisUtil.subscribe(publishKey, new Listener(server));
					}
				}).start();
			}
		}
	}
}
