package me.yotkaz.torrent.client.file;

public class FileInfo {
	private String md5;
	private String name;
	private long size;
	private String path;
	
	public FileInfo(String md5, String name, String path, long size) {
		super();
		this.md5 = md5;
		this.name = name;
		this.path = path;
		this.size = size;
	}
	
	public FileInfo(String md5, String name, long size) {
		super();
		this.md5 = md5;
		this.name = name;
		this.size = size;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((md5 == null) ? 0 : md5.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FileInfo other = (FileInfo) obj;
		if (md5 == null) {
			if (other.md5 != null)
				return false;
		} else if (!md5.equals(other.md5))
			return false;
		return true;
	}

	public String getMd5() {
		return md5;
	}

	public String getName() {
		return name;
	}

	public long getSize() {
		return size;
	}
	
	public String getPath(){
		return path;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	public void setPath(String path){
		this.path = path;
	}
}