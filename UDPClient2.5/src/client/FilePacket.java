package client;

import java.util.ArrayList;
import java.util.Arrays;

public class FilePacket {
	
	public static final byte[] DELIMITER = "++".getBytes();

	Header header;
	byte[] chunk;
	public FilePacket(Header header, byte[] chunk) {
		super();
		this.header = header;
		this.chunk = chunk;
	}
	
	public byte[] mergeProp() throws Exception{
		byte[] fullPacket;
		
		String header = this.getHeader().getFileName() + "++" + this.getHeader().getPcktNumInt() + "++"
				+ this.getHeader().getFileSizeInt() + "++";
		System.out.println("Header Length: " + header.length());
		System.out.println("Header: " + header);
		byte[] headerBytes = header.getBytes("ISO-8859-1");
		System.out.println("Header Bytes Length: " + headerBytes.length);
		System.out.println("Chunk Length: " + this.getChunk().length);
		
		fullPacket = new byte[headerBytes.length + this.getChunk().length];
		
		System.out.println("Full Packet Length: " + fullPacket.length);
		
		int k = 0;
		for(int i = 0; i < fullPacket.length; i++){
			if(i < headerBytes.length){
				fullPacket[i] = headerBytes[i];
			}
			else if(i >= headerBytes.length){
				fullPacket[i] = this.getChunk()[k];
				k++;
			}
		}
		
		System.out.println("Full Packet: " + new String(fullPacket,"ISO-8859-1"));
		return fullPacket;
	}
	
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public byte[] getChunk() {
		return chunk;
	}
	public void setChunk(byte[] chunk) {
		this.chunk = chunk;
	}
	
	
}
