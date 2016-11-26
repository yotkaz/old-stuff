package me.yotkaz.torrent.client.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import me.yotkaz.torrent.client.file.DownloadInfo;
import me.yotkaz.torrent.client.file.FileInfo;
import me.yotkaz.torrent.client.main.Client;
import me.yotkaz.torrent.client.ping.PingTCP;


public class ClientTCP implements Runnable {
		
	private Client client;
	private Properties properties;
	private InetAddress serverAddress;
	private int serverPort;
	private Socket server;
	private ServerSocket localServerSocket;
	private int localServerPort;
	
	private OutputStream os;
	private OutputStreamWriter osw;
	private BufferedWriter bw;
	
	private InputStream is;
	private InputStreamReader isr;
	private BufferedReader br;
	
	public ClientTCP(Client client) throws UnknownHostException {
		super();
		this.client = client;
		properties = client.getProperties();
		serverAddress = InetAddress.getByName(properties.getProperty("serverAddress"));
		serverPort = Integer.parseInt(properties.getProperty("serverPort"));
	}

	@Override
	public void run() {
		try {
			localServerSocket = new ServerSocket(0);
			localServerPort = localServerSocket.getLocalPort();
		} catch (IOException e) {
			e.printStackTrace();
		}
		addMe();
		refreshUsersList();
		refreshFilesList();
		startLocalServerTCP();
		startPingTCP();
	}
	
	private void startPingTCP(){
		new Thread(new PingTCP(serverAddress, serverPort, localServerPort,
				Integer.parseInt(properties.getProperty("pingSleepTime")))).start();
	}
	
	private void startLocalServerTCP(){
		new Thread(new LocalServerTCP(localServerSocket, client)).start();
	}
	
	private void addMe(){
		try {
			server = new Socket(serverAddress, serverPort);
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
		try {
			os = server.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			bw.write("0100");
			bw.newLine();
			bw.write(localServerPort+"");
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void refreshFilesList(){
		new Thread(new ClientFilesList(client)).start();
	}
	
	public void refreshUsersList(){
		Set<InetSocketAddress> tmpUsers = new HashSet<InetSocketAddress>();
		try {
			server = new Socket(serverAddress, serverPort);
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
		try {
			os = server.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			bw.write("0000");
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			is = server.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String line;
		try {
			while((line = br.readLine()) != null){
				String miniTab[] = line.split(":");
				if(miniTab[0].equals(localServerSocket.getInetAddress().getHostAddress())
						|| Integer.parseInt(miniTab[1]) == localServerPort){
					continue;
				}
				InetSocketAddress tmp = new InetSocketAddress(
						InetAddress.getByName(miniTab[0]),
						Integer.parseInt(miniTab[1]));
				tmpUsers.add(tmp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			server.close();
		} catch (IOException e) {
		}
		for(InetSocketAddress insa : client.getUsers()){
			if(!tmpUsers.contains(insa)){
				client.getUsers().remove(insa);
			}
		}
		for(InetSocketAddress insa : tmpUsers){
			if(!client.getUsers().contains(insa)){
				client.getUsers().add(insa);
			}
		}		
	}
	
	public void startAllDownloadThreads(){
		for(String md5 : client.getDownloads().keySet()){
			ClientPull cPull = new ClientPull(client, md5);
			Thread thread = new Thread(cPull);
			client.getDownloadThreads().put(md5, cPull);
			thread.start();
		}
	}
	
	public void saveDownloads(){
		for(DownloadInfo di : client.getDownloads().values()){
			File tmpFile = new File("tmp/" + di.getFile().getName() + ".tmp");
			File tmpFile2 = new File("tmp/" + di.getFile().getName() + ".tmp2");
			FileOutputStream fos = null;
			OutputStreamWriter osw = null;
			BufferedWriter bw = null;
			try {
				fos = new FileOutputStream(tmpFile2);
				osw = new OutputStreamWriter(fos);
				bw = new BufferedWriter(osw);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw.write(di.getFile().getMd5());
				bw.newLine();
				bw.write(di.getFile().getName());
				bw.newLine();
				bw.write(di.getFile().getPath());
				bw.newLine();
				bw.write(di.getFile().getSize() + "");
				bw.newLine();
				bw.write(di.getBlocksNumber() + "");
				bw.newLine();
				bw.write(di.getBlockSize() + "");
				bw.newLine();
				for(int i=0; i<di.getBlocksNumber(); i++){
					bw.write(di.getBlocks()[i]);
					bw.newLine();
				}
				bw.flush();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(tmpFile.exists()){
				tmpFile.delete();
			}
			
			try {
				Files.move(tmpFile2.toPath(), tmpFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	private void prepareDownloadInfo(String md5){
		int blockSize = Integer.parseInt(client.getProperties().getProperty("blockSize"));
		long size = client.getFilesInfo().get(md5).getSize();
		String name = client.getFilesInfo().get(md5).getName();
		String filePath = client.getProperties().getProperty("homeDirectory") + "/download/"+name;
		int nob = (int) (size / blockSize);
		if((size % blockSize) != 0){
			nob++;
		}
		
		File downloadFile = new File(filePath);
		System.out.println("file created: " + downloadFile.getAbsolutePath());
		if(!downloadFile.exists()){
			try {
				RandomAccessFile f = new RandomAccessFile(filePath, "rw");
				f.setLength(size);
				f.close();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		
		
		
		client.getDownloads().put(md5, new DownloadInfo(
				new FileInfo(
					md5, 
					name,
					filePath,
					size
					),
				nob,
				blockSize,
				new int[nob]
				));
	}
	
	public void pullFile(String md5){
		prepareDownloadInfo(md5);
		ClientPull cPull = new ClientPull(client, md5);
		Thread thread = new Thread(cPull);
		client.getDownloadThreads().put(md5, cPull);
		thread.start();
	}
	
	public void pullStop(String md5){
		client.getDownloadThreads().get(md5).setSuspended(true);
	}
	
	public void pullResume(String md5){
		ClientPull cPull = client.getDownloadThreads().get(md5);
		cPull.setSuspended(false);
		synchronized(cPull.getLock()){
			cPull.notify();
		}
	}
}