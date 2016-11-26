package me.yotkaz.torrent.client.ping;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Set;

public class PingTCP implements Runnable {

	private Set<InetAddress> users;
	private Socket server;
	private InetAddress serverAddress;
	private int serverPort;
	private int localServerPort;
	private int sleepTime;
	private OutputStream os;
	private OutputStreamWriter osw;
	private BufferedWriter bw;
	
	public PingTCP(InetAddress serverAddress, int serverPort, int localServerPort,
			int sleepTime) {
		super();
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.localServerPort = localServerPort;
		this.sleepTime = sleepTime;
	}

	@Override
	public void run() {
		while(true){
			try {
				server = new Socket(serverAddress, serverPort);
			} catch (IOException e1) {
				e1.printStackTrace();
			}	
			try {
				os = server.getOutputStream();
				osw = new OutputStreamWriter(os);
				bw = new BufferedWriter(osw);
				bw.write("0300");
				bw.newLine();
				bw.write(localServerPort+"");
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		}
}
