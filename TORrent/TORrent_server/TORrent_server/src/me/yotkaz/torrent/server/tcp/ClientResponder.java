package me.yotkaz.torrent.server.tcp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

public class ClientResponder implements Runnable {

	private Map<String, Boolean> users;
	private Socket socket;
	private OutputStream os;
	private OutputStreamWriter osw;
	private BufferedWriter bw;
	
	public ClientResponder(Map<String, Boolean> users, Socket socket) {
		super();
		this.users = users;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			for(String user : users.keySet()){				
				bw.write(user);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
