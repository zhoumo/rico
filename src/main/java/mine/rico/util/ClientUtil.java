package mine.rico.util;

import java.util.List;

import iqq.im.QQActionListener;
import iqq.im.QQClient;
import iqq.im.bean.QQBuddy;
import iqq.im.bean.QQCategory;
import iqq.im.event.QQActionEvent;

public class ClientUtil {

	public static void getUserInfo(QQClient client) {
		client.getUserInfo(client.getAccount(), new QQActionListener() {

			public void onActionEvent(QQActionEvent event) {
				System.out.println("LOGIN_STATUS:" + event.getType() + ":" + event.getTarget());
			}
		});
	}

	public static void getBuddyList(QQClient client) {
		client.getBuddyList(new QQActionListener() {

			@SuppressWarnings("unchecked")
			public void onActionEvent(QQActionEvent event) {
				if (event.getType() != QQActionEvent.Type.EVT_OK) {
					return;
				}
				List<QQCategory> qqCategoryList = (List<QQCategory>) event.getTarget();
				for (QQCategory group : qqCategoryList) {
					List<QQBuddy> buddyList = group.getBuddyList();
					for (QQBuddy friend : buddyList) {
						System.out.println("name: " + friend.getNickname() + ", uin: " + friend.getUin());
					}
				}
			}
		});
	}
}
