package me.yotkaz.torrent.server.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerTCP implements Runnable {

	private Map<String, Boolean> users;
	private int port;
	private ServerSocket serverSocket;
	private ExecutorService exs;
	private Socket incomingSocket;
	
	public ServerTCP(Map<String, Boolean> users, int poolSize, int port) {
		super();
		this.users = users;
		this.port = port;
		exs = Executors.newFixedThreadPool(poolSize);
	}
		
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			
			while(true){
				incomingSocket = serverSocket.accept();
				exs.execute(new ClientHandler(users, incomingSocket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
