package mine.rico;

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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import mine.rico.module.Robot;
import mine.rico.util.ClientUtil;

public class Server {

	private QQClient client;

	private Map<String, QQMsg> msgMap;

	public Server() {
		client = new WebQQClient("3166262398", "zhoumo", new QQNotifyHandlerProxy(this), new ThreadActorDispatcher());
		msgMap = new ConcurrentHashMap<String, QQMsg>();
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
				}
			}
		};
		client.login(QQStatus.ONLINE, listener);
	}

	public void sendMsg() {
		QQMsg msg = msgMap.get("272500955");
		if (msg == null) {
			return;
		}
		QQMsg newMsg = new QQMsg();
		newMsg.setType(msg.getType());
		newMsg.setTo(msg.getFrom());
		newMsg.addContentItem(new TextItem("hello"));
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
	public void processChatMsg(QQNotifyEvent event) {
		QQMsg msg = (QQMsg) event.getTarget();
		StringBuilder text = new StringBuilder(1000);
		for (ContentItem item : msg.getContentList()) {
			if (item.getType() == ContentItem.Type.TEXT) {
				text.append(((TextItem) item).getContent().trim().replaceAll("\t|\r", "")).append("\n");
			}
		}
		System.out.println("message: " + text.toString());
		client.sendMsg(autoReply(msg, text.toString()), new QQActionListener() {

			public void onActionEvent(QQActionEvent event) {
			}
		});
		msgMap.put("272500955", msg);
	}

	private QQMsg autoReply(QQMsg msg, String text) {
		QQMsg newMsg = new QQMsg();
		newMsg.setType(msg.getType());
		if (msg.getType() == QQMsg.Type.GROUP_MSG) {
			newMsg.setGroup(msg.getGroup());
		}
		newMsg.setTo(msg.getFrom());
		newMsg.addContentItem(new TextItem(Robot.chat(text)));
		return newMsg;
	}
}
