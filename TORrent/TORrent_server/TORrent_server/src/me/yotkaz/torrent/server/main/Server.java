package me.yotkaz.torrent.server.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import me.yotkaz.torrent.server.ping.PingChecker;
import me.yotkaz.torrent.server.tcp.ServerTCP;

public class Server {
	private Map<String, Boolean> users = new ConcurrentHashMap<String, Boolean>();
	private Properties properties = new Properties();
	private File file = new File("rsc/server.properties");
	
	public static void main(String[] args) {
		Server server = new Server();
		server.startCMDControl();
		server.loadProperties();
		server.startServerTCP();
		server.startPingChecker();
	}
	
	private void loadProperties(){
		try {
			InputStream is = new FileInputStream(file);
			properties.load(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void startServerTCP(){
		int poolSizeTCP = Integer.parseInt(
				properties.getProperty("poolSizeTCP", "10"));
		int portTCP = Integer.parseInt(
				properties.getProperty("portTCP", "16669"));
		
		new Thread(new ServerTCP(users, poolSizeTCP, portTCP)).start();
	}
	
	private void startPingChecker(){
		int sleepTime = Integer.parseInt(
				properties.getProperty("pingCheckerSleepTime", "100000"));
		new Thread(new PingChecker(users, sleepTime)).start();
	}
	
	private void startCMDControl(){
		new Thread(new CMD(this)).start();
	}
	
}