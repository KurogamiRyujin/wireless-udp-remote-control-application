package client;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.Queue;

public class PacketsHolder {

	FileInputStream fileInput;
	Queue<FilePacket> pcktQ;
	String fileName;
	int fileSize;
	int pcktMaxSize;
	
	public PacketsHolder(File file, int pckMaxSize) throws Exception{
		this.fileInput = new FileInputStream(file);
		this.pcktQ = new LinkedList<FilePacket>();
		this.fileName = file.getName();
		this.fileSize = this.fileInput.available();
		this.pcktMaxSize = pckMaxSize;
		
		this.parseFile();
	}
	
	private void parseFile() throws Exception{
		int i = 0;
		
		while(this.fileInput.available() > 0){
			byte[] pcktBody = new byte[this.pcktMaxSize];
			if(this.fileInput.available() >= this.pcktMaxSize){
				this.fileInput.read(pcktBody, 0, this.pcktMaxSize);
			}
			else{
				this.fileInput.read(pcktBody, 0, this.fileInput.available());
			}
			byte[] chunk = pcktBody;
			FilePacket pckt = new FilePacket(new Header(this.fileName, i, this.fileSize),chunk);
			pcktQ.offer(pckt);
			i++;
		}
	}

	public Queue<FilePacket> getPcktQ() {
		return pcktQ;
	}

	public void setPcktQ(Queue<FilePacket> pcktQ) {
		this.pcktQ = pcktQ;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getPcktMaxSize() {
		return pcktMaxSize;
	}

	public void setPcktMaxSize(int pcktMaxSize) {
		this.pcktMaxSize = pcktMaxSize;
	}
	
	
}
