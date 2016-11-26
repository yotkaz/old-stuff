package me.yotkaz.torrent.server.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;

public class ClientHandler implements Runnable {

	private Map<String, Boolean> users;
	private Socket socket;
	private InputStream is;
	private InputStreamReader isr;
	private BufferedReader br;
	
	public ClientHandler(Map<String, Boolean> users, Socket socket) {
		super();
		this.users = users;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			
			String line;
			if((line = br.readLine()) != null){
				switchCode(line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void switchCode(String code) throws IOException{
		System.out.println(socket.toString() + ": " + code);
		int clientServerPort = 0;
		InetAddress ina = null;
		
		switch(code){
		case "0100":
		case "0200":
		case "0300":
			ina = socket.getInetAddress();
			String line;
			if((line = br.readLine()) != null){
				clientServerPort = Integer.parseInt(line);
			}
			break;
		}
		
		switch(code){
		case "0000": //get list
			new Thread(new ClientResponder(users, socket)).start();
			break;
		case "0100": //add user
			users.put(ina.getHostAddress() + ":" + clientServerPort, true);
			socket.close();
			break;
		case "0200": //remove user
			users.remove(ina.getHostAddress() + ":" + clientServerPort);
			socket.close();
			break;
		case "0300": //ping from user
			users.replace(ina.getHostAddress() + ":" + clientServerPort, new Boolean(true));
			socket.close();
			break;
		}
	}
}
