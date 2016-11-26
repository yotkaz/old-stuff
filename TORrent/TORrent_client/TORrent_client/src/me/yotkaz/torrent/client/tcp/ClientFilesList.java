package me.yotkaz.torrent.client.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import me.yotkaz.torrent.client.file.FileInfo;
import me.yotkaz.torrent.client.main.Client;

public class ClientFilesList implements Runnable {
	
	private Client client;
	private Socket socket;
	
	private InputStream is;
	private InputStreamReader isr;
	private BufferedReader br;
	private OutputStream os;
	private OutputStreamWriter osw;
	private BufferedWriter bw;
	
	public ClientFilesList(Client client) {
		super();
		this.client = client;
	}

	@Override
	public void run() {
		HashMap<String, Set<InetSocketAddress>> tmpMapUsers = new HashMap<String, Set<InetSocketAddress>>();
		HashMap<String, FileInfo> tmpMapInfo = new HashMap<String, FileInfo>();
		for(InetSocketAddress user : client.getUsers()){
			try {
				socket = new Socket(user.getAddress(), user.getPort());
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			try {
				os = socket.getOutputStream();
				osw = new OutputStreamWriter(os);
				bw = new BufferedWriter(osw);
				bw.write("1000");
				bw.newLine();
				bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}	
			try {
				is = socket.getInputStream();
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
			} catch (IOException e) {
				e.printStackTrace();
			}
			int counter = 0;
			String line;
			try {
				String name = "";
				long size = 0;
				String md5 = "";
				while((line = br.readLine()) != null){
					switch(counter){
					case 0:
						md5 = line;
						break;
					case 1:
						name = line;
						break;
					case 2:
						size = Integer.parseInt(line);
						break;
					}
					counter++;
					if(counter == 3){
						counter = 0;
						tmpMapInfo.put(md5, new FileInfo(md5, name, size));
						if(tmpMapUsers.containsKey(md5)){
							tmpMapUsers.get(md5).add(user);
						}
						else{
							Set<InetSocketAddress> tmp = Collections.newSetFromMap(
									new ConcurrentHashMap<InetSocketAddress, Boolean>());
							tmp.add(user);
							tmpMapUsers.put(md5, tmp);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for(Entry<String, Set<InetSocketAddress>> entry : client.getFilesUsers().entrySet()){
				if(!tmpMapUsers.containsKey(entry.getKey())){
					client.getFilesUsers().remove(entry.getKey());
				}
				else{
					for(InetSocketAddress insa : entry.getValue()){
						if(!tmpMapUsers.get(entry.getKey()).contains(insa)){
							client.getFilesUsers().get(entry.getKey()).remove(insa);
						}
					}
				}
			}
			
			for(Entry<String, Set<InetSocketAddress>> entry : tmpMapUsers.entrySet()){
				if(!client.getFilesUsers().containsKey(entry.getKey())){
					client.getFilesUsers().put(entry.getKey(), entry.getValue());
				}
				else{
					for(InetSocketAddress insa : entry.getValue()){
						if(!client.getFilesUsers().get(entry.getKey()).contains(insa)){
							client.getFilesUsers().get(entry.getKey()).add(insa);
						}
					}
				}
			}
			
			for(Entry<String, FileInfo> entry : client.getFilesInfo().entrySet()){
				if(!tmpMapInfo.containsKey(entry.getKey())){
					client.getFilesInfo().remove(entry.getKey());
				}
			}
			
			for(Entry<String, FileInfo> entry :tmpMapInfo.entrySet()){
				if(!client.getFilesInfo().containsKey(entry.getKey())){
					client.getFilesInfo().put(entry.getKey(), entry.getValue());
				}
			}
			
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
