package mine.rico;

import mine.rico.module.TopicListener;
import mine.rico.util.RedisUtil;

public class Launcher {

	public static final String PUB_KEY = "TEST";

	public static void main(String[] args) throws InterruptedException {
		final Server server = new Server();
		server.login();
		boolean isSub = false;
		while (true) {
			Thread.sleep(10000);
			if (RedisUtil.exists(PUB_KEY) && Server.RUNNING) {
				for (String key : RedisUtil.hGet(PUB_KEY).keySet()) {
					if (RedisUtil.exists(key) && !isSub) {
						new Thread(new Runnable() {

							public void run() {
								RedisUtil.subscribe(PUB_KEY, new TopicListener(server));
							}
						}).start();
						isSub = true;
					}
				}
			}
		}
	}
}
