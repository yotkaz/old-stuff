package me.yotkaz.torrent.client.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import me.yotkaz.torrent.client.file.DownloadInfo;
import me.yotkaz.torrent.client.file.FileInfo;
import me.yotkaz.torrent.client.file.MD5;
import me.yotkaz.torrent.client.tcp.ClientPull;
import me.yotkaz.torrent.client.tcp.ClientTCP;

public class Client {
	private static final String PROPERTIES_FILE_PATH = "rsc/client.properties";
	
	private Set<InetSocketAddress> users = Collections.newSetFromMap(new ConcurrentHashMap<InetSocketAddress, Boolean>());
	private Map<String, ClientPull> downloadThreads = new ConcurrentHashMap<String, ClientPull>();
	private Map<String, Set<InetSocketAddress>> filesUsers = new ConcurrentHashMap<String, Set<InetSocketAddress>>();
	private Map<String, FileInfo> filesInfo = new ConcurrentHashMap<String, FileInfo>();
	private Map<String, FileInfo> myFilesInfo = new ConcurrentHashMap<String, FileInfo>();
	private Map<String, DownloadInfo> downloads = new ConcurrentHashMap<String, DownloadInfo>();
	private Properties properties = new Properties();
	private File file = new File(PROPERTIES_FILE_PATH);
	private ClientTCP clientTCP;
	
	public static void main(String[] args) {
		Client client = new Client();
		client.startCMDControl();
		client.loadProperties();
		client.getDirArg(args);
		client.checkHomeDirectory();
		client.loadHomeDirectoryFiles();
		client.loadDownloads();
		client.startClientTCP();
	}
	
	private void getDirArg(String[] args){
		if(args.length > 0){
			properties.setProperty("homeDirectory", args[0]);
		}
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
	
	private void startCMDControl(){
		new Thread(new CMD(this)).start();
	}
	
	private void checkHomeDirectory(){
		File dir = new File(properties.getProperty("homeDirectory"));
		File tmpDir = new File(properties.getProperty("homeDirectory") + "/tmp");
		if(!dir.exists()){
			dir.mkdir();
		}
		if(!tmpDir.exists()){
			tmpDir.mkdir();
		}
	}
	
	public void loadHomeDirectoryFiles(){
		myFilesInfo.clear();
		File dir = new File(properties.getProperty("homeDirectory"));
		File [] tabFiles = dir.listFiles();
		for(File f : tabFiles){
			if (!f.isFile()) continue;
			String name = f.getName();
			String path = f.getAbsolutePath();
			String md5 = MD5.getChecksum(f);
			long size = f.length();
			myFilesInfo.put(md5, new FileInfo(md5, name, path, size));
		}
	}
	
	private void loadDownloads(){
		File tmpDir = new File(properties.getProperty("homeDirectory") + "/tmp");
		File [] tabFiles = tmpDir.listFiles();
		for(File f : tabFiles){
			if (!f.isFile()) continue;
			if (!f.getAbsolutePath().endsWith(".tmp")) continue;
			InputStreamReader isr = null;
			BufferedReader br = null;
			try {
				isr = new InputStreamReader(new FileInputStream(f));
				br = new BufferedReader(isr);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String line;
			try {
				String md5 = "";
				String name = "";
				String path = "";
				long size = 0;
				int blocksNumber = 0;
				int blockSize = 0;
				int[] blocks = null;
				int counter = 0;
				while((line = br.readLine()) != null){
					switch(counter){
					case 0:
						md5 = line;
						break;
					case 1:
						name = line;
						break;
					case 2:
						path = line;
						break;
					case 3:
						size = Integer.parseInt(line);
						break;
					case 4:
						blocksNumber = Integer.parseInt(line);
						blocks = new int[blocksNumber];
						break;
					case 5:
						blockSize = Integer.parseInt(line);
						break;
					}
					if(counter > 5){
						blocks[counter-6]=Integer.parseInt(line);
					}
				}
				downloads.put(md5, new DownloadInfo(new FileInfo(md5, name, path, size), 
						blocksNumber, blockSize, blocks));
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void startClientTCP(){
		try {
			clientTCP = new ClientTCP(this);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		new Thread(clientTCP).start();
	}
	
	public Set<InetSocketAddress> getUsers() {
		return users;
	}
	
	public Map<String, ClientPull> getDownloadThreads() {
		return downloadThreads;
	}

	public Map<String, Set<InetSocketAddress>> getFilesUsers() {
		return filesUsers;
	}

	public Map<String, FileInfo> getFilesInfo() {
		return filesInfo;
	}
	
	public Map<String, FileInfo> getMyFilesInfo() {
		return myFilesInfo;
	}
	
	public Map<String, DownloadInfo> getDownloads() {
		return downloads;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public ClientTCP getClientTCP(){
		return clientTCP;
	}
}
