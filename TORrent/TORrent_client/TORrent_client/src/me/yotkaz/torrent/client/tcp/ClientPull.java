package me.yotkaz.torrent.client.tcp;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.Socket;

import me.yotkaz.torrent.client.main.Client;


public class ClientPull implements Runnable{
	
	private Client client;
	private String md5;
	private boolean complete = false;
	private boolean suspended = false;
	private Object lock = new Object();
	private boolean showLog = false;

	private OutputStream os;
	private OutputStreamWriter osw;
	private BufferedWriter bw;
	
	public void setSuspended(boolean value){
		suspended = value;
	}
	
	public void setShowLog(boolean value){
		showLog = value;
	}
	
	public Object getLock(){
		return lock;
	}
	
	public ClientPull(Client client, String md5) {
		super();
		this.client = client;
		this.md5 = md5;
	}

	@Override
	public void run() {
		int counter = 0;
		
		while(complete == false){	
			for(InetSocketAddress insa : client.getFilesUsers().get(md5)){
				try {
					while(suspended){
						synchronized (lock) {
						    wait();
						}
					}
				} catch (InterruptedException e) {
						e.printStackTrace();
				}
							
				Socket socket;
				try {
					int innerCounter = 0;
					while(client.getDownloads().get(md5).getBlocks()[counter] == 1){
						counter++;
						if(counter == client.getDownloads().get(md5).getBlocksNumber()){
							counter = 0;
						}
						innerCounter++;
						if(innerCounter == client.getDownloads().get(md5).getBlocksNumber()){
							complete = true;
							break;
						}
					}	
					if(complete == true) break;
					
					socket = new Socket(insa.getAddress(), insa.getPort());
					
					byte[] block = null;
					if(counter == client.getDownloads().get(md5).getBlocksNumber() - 1){
						block = new byte[(int) (client.getDownloads().get(md5).getFile().getSize()-
								(client.getDownloads().get(md5).getBlockSize() * (client.getDownloads().get(md5).getBlocksNumber()-1)))];
					}
					else{
						block = new byte[client.getDownloads().get(md5).getBlockSize()];
					}
									
					os = socket.getOutputStream();
					osw = new OutputStreamWriter(os);
					bw = new BufferedWriter(osw);
					bw.write("2000");
					bw.newLine();
					bw.write(md5);
					bw.newLine();
					bw.write("" + client.getDownloads().get(md5).getBlockSize() * counter);
					bw.newLine();
					bw.write("" + block.length);
					bw.newLine();
					bw.flush();
					
					InputStream is = socket.getInputStream();
					DataInputStream dis = new DataInputStream(is);
					dis.read(block);
					
					RandomAccessFile raf = new RandomAccessFile(
							client.getDownloads().get(md5).getFile().getPath(), "rw");
					raf.seek(counter * client.getDownloads().get(md5).getBlockSize());
					raf.write(block);
					raf.close(); 
					dis.close();
					socket.close();
					
					if(showLog){
						System.out.println(
							"block #" + counter 
							+	" ::: size " + block.length
						);
					}
					
					client.getDownloads().get(md5).getBlocks()[counter] = 1;
					counter++;
					if(counter == client.getDownloads().get(md5).getBlocksNumber()){
						counter = 0;
					}
									
				} catch (IOException e) {
					System.err.println(e.getMessage());
					continue;
				}
			}
		}
	}
}
