package me.yotkaz.torrent.client.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;

import me.yotkaz.torrent.client.file.FileInfo;

public class CMD implements Runnable {
	private static final String HELP_FILE_PATH = "rsc/cmd_help";
	
	private Client client;
	private Properties properties;
	private Scanner scanner = new Scanner(System.in);

	public CMD(Client client) {
		super();
		this.client = client;
		properties = client.getProperties();
	}

	@Override
	public void run() {
		hello();
		try {
			cmdControl();
		} catch (IOException e) {
			e.printStackTrace();
		}
		scanner.close();
	}
	
	private void cmdControl() throws IOException{
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			action(line);
		}
	}
	
	private void action(String command) throws IOException{
		command = command.toLowerCase();
		switch(command.toLowerCase()){
		case "exit":
			System.exit(0);
			break;
		case "help": 
			printHelp();
			break;
		case "show_users_list":
			printUsersList();
			break;
		case "show_my_files":
			printMyFilesList();
			break;
		case "refresh_my_files":
			client.loadHomeDirectoryFiles();
			break;
		case "show_files_list":
			printFilesList();
			break;
		case "get_users_list":
			client.getClientTCP().refreshUsersList();
			break;
		case "get_files_list":
			client.getClientTCP().refreshFilesList();
			break;
		case "save_downloads":
			client.getClientTCP().saveDownloads();
			break;
		case "pull":
			pull();
			break;
		case "pull_stop":
			pullStop();
			break;
		case "pull_resume":
			pullResume();
			break;
		case "show_download_log":
			showDownloadLog();
			break;
		case "hide_download_log":
			hideDownloadLog();
			break;
		}
		
	}
	
	private void hello(){
		System.out.println("Hello, this is TORrent_client\n"
				+ "Type help to show available commands.");
	}
	
	private void printHelp() throws IOException{
		File helpFile = new File(HELP_FILE_PATH);
		System.out.println("\nFiles to share should be in: " + properties.getProperty("homeDirectory"));
		System.out.println("Downloaded files will be in subdirectory download.\n");
		FileReader fr = new FileReader(helpFile);
		BufferedReader br = new BufferedReader(fr);
		String line;
		while((line = br.readLine()) != null){
			System.out.println(line);
		}
		br.close();
	}
	
	private void printUsersList(){
		for(InetSocketAddress insa : client.getUsers()){
			System.out.println(insa.getHostName() + ":" + insa.getPort());
		}
	}
	
	private void printMyFilesList(){
		for(FileInfo fi : client.getMyFilesInfo().values()){
			System.out.println("----------------------------");
			System.out.println(fi.getName());
			System.out.println("size: " + fi.getSize());
			System.out.println("md5: " + fi.getMd5());
		}
	}
	
	private void printFilesList(){
		for(FileInfo fi : client.getFilesInfo().values()){
			System.out.println("----------------------------");
			System.out.println(fi.getName());
			System.out.println("size: " + fi.getSize());
			System.out.println("md5: " + fi.getMd5());
			if(client.getFilesUsers().containsKey(fi.getMd5())
					&& (!client.getFilesUsers().get(fi.getMd5()).isEmpty())){
				System.out.println("clients: " + 
					client.getFilesUsers().get(fi.getMd5()).size());
			}
		}
	}
	
	private void pull(){
		String md5 = promptString("md5");
		client.getClientTCP().pullFile(md5);
	}
	
	private void pullStop(){
		String md5 = promptString("md5");
		client.getClientTCP().pullStop(md5);
	}
	
	private void pullResume(){
		String md5 = promptString("md5");
		client.getClientTCP().pullResume(md5);
	}
	
	private void showDownloadLog(){
		String md5 = promptString("md5");
		client.getDownloadThreads().get(md5).setShowLog(true);
	}
	
	private void hideDownloadLog(){
		String md5 = promptString("md5");
		client.getDownloadThreads().get(md5).setShowLog(false);
	}
	
	private int promptInt(String argName){
		System.out.println("Please type " + argName + ":");
		return scanner.nextInt();
	}
	
	private String promptString(String argName){
		System.out.println("Please type " + argName + ":");
		return scanner.nextLine();
	}
}