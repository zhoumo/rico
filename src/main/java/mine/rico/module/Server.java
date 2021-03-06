package mine.rico.module;

import iqq.im.QQActionListener;
import iqq.im.QQClient;
import iqq.im.WebQQClient;
import iqq.im.actor.ThreadActorDispatcher;
import iqq.im.bean.QQMsg;
import iqq.im.bean.QQStatus;
import iqq.im.bean.content.ContentItem;
import iqq.im.bean.content.TextItem;
import iqq.im.core.QQConstants;
import iqq.im.event.QQActionEvent;
import iqq.im.event.QQNotifyEvent;
import iqq.im.event.QQNotifyEventArgs;
import iqq.im.event.QQNotifyHandler;
import iqq.im.event.QQNotifyHandlerProxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import javax.imageio.ImageIO;

import mine.rico.module.Robot;
import mine.rico.util.ClientUtil;
import mine.rico.util.ConfigUtil;
import mine.rico.util.RedisUtil;
import mine.rico.util.StringUtil;

public class Server {

	private QQClient client;

	public static boolean RUNNING = false;

	public Server() {
		client = new WebQQClient(ConfigUtil.get(ConfigUtil.QQ_NUMBER), ConfigUtil.get(ConfigUtil.QQ_PASSWORD), new QQNotifyHandlerProxy(this), new ThreadActorDispatcher());
	}

	public void login() {
		String userAgent = "Mozilla/5.0 ({1}; {2}; {3}) AppleWebKit/537.36 (KHTML, like Gecko) {0} Safari/537.36";
		userAgent = MessageFormat.format(userAgent, QQConstants.USER_AGENT, System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));
		client.setHttpUserAgent(userAgent);
		QQActionListener listener = new QQActionListener() {

			public void onActionEvent(QQActionEvent event) {
				if (event.getType() == QQActionEvent.Type.EVT_OK) {
					ClientUtil.getUserInfo(client);
					ClientUtil.getBuddyList(client);
					client.beginPollMsg();
					RUNNING = true;
				}
			}
		};
		client.login(QQStatus.ONLINE, listener);
	}

	public void sendMsg(QQMsg msg, String text) {
		if (msg == null) {
			return;
		}
		QQMsg newMsg = new QQMsg();
		newMsg.setType(msg.getType());
		newMsg.setTo(msg.getFrom());
		newMsg.addContentItem(new TextItem(text));
		client.sendMsg(newMsg, new QQActionListener() {

			public void onActionEvent(QQActionEvent event) {
			}
		});
	}

	@QQNotifyHandler(QQNotifyEvent.Type.CAPACHA_VERIFY)
	protected void processCapachaVerify(QQNotifyEvent event) throws IOException {
		System.out.print("capacha: ");
		ImageIO.write(((QQNotifyEventArgs.ImageVerify) event.getTarget()).image, "png", new File("verify.png"));
		client.submitVerify(new BufferedReader(new InputStreamReader(System.in)).readLine(), event);
	}

	@QQNotifyHandler(QQNotifyEvent.Type.KICK_OFFLINE)
	protected void processKickOffLine(QQNotifyEvent event) {
		System.out.println("kick offline: " + (String) event.getTarget());
	}

	@QQNotifyHandler(QQNotifyEvent.Type.CHAT_MSG)
	protected void processChatMsg(QQNotifyEvent event) {
		QQMsg msg = (QQMsg) event.getTarget();
		StringBuilder text = new StringBuilder(1000);
		for (ContentItem item : msg.getContentList()) {
			if (item.getType() == ContentItem.Type.TEXT) {
				text.append(((TextItem) item).getContent().trim().replaceAll("\t|\r", "")).append("\n");
			}
		}
		String uin = String.valueOf(msg.getFrom().getUin());
		boolean reply = true;
		for (String publishKey : ConfigUtil.get(ConfigUtil.PUBLISH_KEYS).split(",")) {
			if (text.toString().trim().equals("订阅" + publishKey)) {
				RedisUtil.set(publishKey + uin, StringUtil.serialize(msg));
				RedisUtil.hSet(publishKey, uin);
				sendMsg(msg, "订阅成功！");
				reply = false;
			}
			if (text.toString().trim().equals("取消" + publishKey)) {
				RedisUtil.del(publishKey + uin);
				RedisUtil.hDel(publishKey, uin);
				sendMsg(msg, "取消成功！");
				reply = false;
			}
		}
		if (reply) {
			sendMsg(msg, Robot.chat(text.toString()));
		}
	}
}
