package me.yotkaz.torrent.client.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public static String getChecksum(File f){
		MessageDigest md = null;
		FileInputStream fis = null;
		
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		try {
			fis = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
 
        byte[] dataBytes = new byte[1024];
 
        int nread = 0; 
        try {
			while ((nread = fis.read(dataBytes)) != -1) {
			  md.update(dataBytes, 0, nread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		};
        byte[] mdbytes = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        try {
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	};
}
