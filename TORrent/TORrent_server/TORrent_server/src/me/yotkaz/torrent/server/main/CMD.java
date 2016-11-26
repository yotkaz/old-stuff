package me.yotkaz.torrent.server.main;

import java.util.Scanner;

public class CMD implements Runnable{
	
	private Server server;
	private Scanner scanner;
	
	public CMD(Server server) {
		super();
		this.server = server;
	}

	private void hello(){
		System.out.println("Hello, TORrent server is running.\n"
				+ "Type exit or EXIT to turn off server.");
	}
	
	private void cmdControl(){
		scanner = new Scanner(System.in);
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			switch(line.toLowerCase()){
			case "exit":
				System.exit(0);
				break;
			}
		}
		scanner.close();
	}
	

	@Override
	public void run() {
		hello();
		cmdControl();
	}
}
