package mine.rico;

import iqq.im.bean.QQMsg;
import mine.rico.util.RedisUtil;
import mine.rico.util.StringUtil;

public class Launcher {

	public static void main(String[] args) throws InterruptedException {
		Server server = new Server();
		server.login();
		while (true) {
			Thread.sleep(10000);
			if (RedisUtil.exists("订阅") && Server.RUNNING) {
				for (String key : RedisUtil.hGet("订阅").keySet()) {
					if (!RedisUtil.exists(key)) {
						continue;
					}
					server.sendMsg((QQMsg) StringUtil.Deserialize(RedisUtil.get(key)), "通知：新年快乐");
				}
			}
		}
	}
}
