package me.yotkaz.torrent.client.tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Arrays;

import me.yotkaz.torrent.client.file.FileInfo;
import me.yotkaz.torrent.client.main.Client;

public class ClientHandler implements Runnable{

	private Client client;
	private Socket socket;
	
	private InputStream is;
	private InputStreamReader isr;
	private BufferedReader br;
	private OutputStream os;
	private OutputStreamWriter osw;
	private BufferedWriter bw;
	private DataOutputStream dos;
	
	public ClientHandler(Client client, Socket socket) {
		super();
		this.client = client;
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
	
	private void switchCode(String code){
		switch(code){ 
		case "1000":	
			sendFilesList();
			break;
		case "2000":
			String md5 = "";
			int start = 0;
			int blockSize = 0;
			String line;
			try {
				if((line = br.readLine()) != null){
					md5 = line;
				}
				if((line = br.readLine()) != null){
					start = Integer.parseInt(line);
				}
				if((line = br.readLine()) != null){
					blockSize = Integer.parseInt(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			sendBlockOfData(md5, start, blockSize);
			break;
		}
	}
	
	private void sendFilesList(){
		try {
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(FileInfo fi : client.getMyFilesInfo().values()){
			try {
				bw.write(fi.getMd5());
				bw.newLine();
				bw.write(fi.getName());
				bw.newLine();
				bw.write(fi.getSize() + "");
				bw.newLine();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void sendBlockOfData(String md5, long start, long blockSize){
		if(client.getProperties().getProperty("showSendLog").toLowerCase().equals("true")){
			System.out.println("send: " + md5 + " ## byteStart: " + start + " ## blockSize " + blockSize);
		}
		String path = client.getMyFilesInfo().get(md5).getPath();
		RandomAccessFile raf = null;
		byte[] block = new byte[(int) blockSize];
		try {
			raf = new RandomAccessFile(path, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			raf.seek(start);
			raf.readFully(block);
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		try {
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
			dos.write(block);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
