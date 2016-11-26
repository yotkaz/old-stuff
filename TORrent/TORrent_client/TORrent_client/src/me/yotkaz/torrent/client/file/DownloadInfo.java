package me.yotkaz.torrent.client.file;

import java.util.Arrays;

public class DownloadInfo {
	private FileInfo file;
	private int blocksNumber;
	private int blockSize;
	private int[] blocks;
	
	public DownloadInfo(FileInfo file, int blocksNumber, int blockSize,
			int[] blocks) {
		super();
		this.file = file;
		this.blocksNumber = blocksNumber;
		this.blockSize = blockSize;
		this.blocks = blocks;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blockSize;
		result = prime * result + Arrays.hashCode(blocks);
		result = prime * result + blocksNumber;
		result = prime * result + ((file == null) ? 0 : file.hashCode());
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
		DownloadInfo other = (DownloadInfo) obj;
		if (blockSize != other.blockSize)
			return false;
		if (!Arrays.equals(blocks, other.blocks))
			return false;
		if (blocksNumber != other.blocksNumber)
			return false;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		return true;
	}
	
	public FileInfo getFile() {
		return file;
	}
	public int getBlocksNumber() {
		return blocksNumber;
	}
	public int getBlockSize() {
		return blockSize;
	}
	public int[] getBlocks() {
		return blocks;
	}
	public void setFile(FileInfo file) {
		this.file = file;
	}
	public void setBlocksNumber(int blocksNumber) {
		this.blocksNumber = blocksNumber;
	}
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}
	public void setBlocks(int[] blocks) {
		this.blocks = blocks;
	}
	
}