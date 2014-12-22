package mine.rico;

public class Launcher {

	public static void main(String[] args) throws InterruptedException {
		Server server = new Server();
		server.login();
		while (true) {
			Thread.sleep(5000);
			server.sendMsg();
		}
	}
}
