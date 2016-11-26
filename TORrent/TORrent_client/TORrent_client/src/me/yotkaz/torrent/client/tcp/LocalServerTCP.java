package me.yotkaz.torrent.client.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.yotkaz.torrent.client.main.Client;

public class LocalServerTCP implements Runnable{
	private ServerSocket socket;
	private Socket incomingSocket;
	private Client client;
	private ExecutorService exs;
	
	public LocalServerTCP(ServerSocket socket, Client client) {
		super();
		this.socket = socket;
		this.client = client;
		this.exs = Executors.newFixedThreadPool(
				Integer.parseInt(client.getProperties().getProperty("localServerPool")));
	}

	@Override
	public void run() {
		while(true){
			try {
				incomingSocket = socket.accept();
				exs.execute(new ClientHandler(client, incomingSocket));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
