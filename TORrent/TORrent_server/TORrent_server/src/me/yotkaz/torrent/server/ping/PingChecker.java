package me.yotkaz.torrent.server.ping;

import java.util.Map;
import java.util.Map.Entry;

public class PingChecker implements Runnable {
	
	private Map<String, Boolean> users;
	private int sleepTime;
	
	public PingChecker(Map<String, Boolean> users, int sleepTime) {
		super();
		this.users = users;
		this.sleepTime = sleepTime;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(Entry<String, Boolean> entry : users.entrySet()){
			if(entry.getValue().booleanValue() == false){
				users.remove(entry.getKey());
			}
			else{
				users.replace(entry.getKey(), new Boolean(false));
			}
		}	
	}
}
