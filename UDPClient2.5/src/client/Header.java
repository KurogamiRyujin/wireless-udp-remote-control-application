package client;

import java.nio.ByteBuffer;

public class Header {

	String fileName;
	int pcktNum;
	int fileSize;
	public Header(String fileName, int pcktNum, int fileSize) {
		super();
		this.fileName = fileName;
		this.pcktNum = pcktNum;
		this.fileSize = fileSize;
	}
	public String getFileName() {
		return fileName;
	}
	
	public byte[] getFileNameBytes() throws Exception{
		return this.fileName.getBytes("ISO-8859-1");
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getPcktNumInt() {
		return pcktNum;
	}
	public byte[] getPcktNum() {
		return ByteBuffer.allocate(4).putInt(pcktNum).array();
	}
	public void setPcktNum(int pcktNum) {
		this.pcktNum = pcktNum;
	}
	public int getFileSizeInt() {
		return fileSize;
	}
	public byte[] getFileSize() {
		return ByteBuffer.allocate(4).putInt(fileSize).array();
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
}
