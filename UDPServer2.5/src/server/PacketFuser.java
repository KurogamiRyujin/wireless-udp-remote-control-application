package server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingWorker;

public class PacketFuser {
	protected ArrayList<FilePacket> packets;
	protected byte[] dataBytes;
	DatagramSocket localSocket;
	protected static InetAddress senderAdd = null;
	protected PacketReceiver receiver;
	protected Timer timer;
	protected static int counter = 0;
	protected int patience = 3;
	
	SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>(){

		@Override
		protected Void doInBackground() throws Exception {
			boolean receiving = true;
			int packetSize = 0;
			int fileSize = 0;
			
			do {
				counter++;
				timer = new Timer();
				timer.schedule(new TimedSender(senderAdd,localSocket), 5000);
				
				byte[] byteSize = new byte[4];
				
				DatagramPacket receivePacketSize = new DatagramPacket(byteSize,byteSize.length);
				localSocket.receive(receivePacketSize);
				timer.cancel();
				patience = 3;
				System.out.println(new String(receivePacketSize.getData(),PacketReceiver.ENCODING));
				int nPacketSize = java.nio.ByteBuffer.wrap(byteSize).getInt();
				senderAdd = receivePacketSize.getAddress();
				
				System.out.println("Receiving file...");
				receiver.receivePacket(nPacketSize);
				fileSize = receiver.getFileSize();
				System.out.println("File Size: " + fileSize);
				if(receiver.getChunk() != null){
					packets.add(receiver.getFilePacket());
					packetSize += receiver.getChunk().length;
					System.out.println("Packet Count: " + packets.size());
					System.out.println("Receiver Chunk byte Length: " + receiver.getChunk().length);
					System.out.println("PacketSize: " + packetSize);
					System.out.println("Receiver File Size" + receiver.getFileSize());
				}
				else System.out.println("Packet Broken!!!");
				if(packetSize >= fileSize){
					packetSize = 0;
					receiving = false;
				}
			}while(receiving);
			
			byte[] receivedPacketNum = ByteBuffer.allocate(4).putInt(-1).array();
			
			DatagramPacket sendPacket = new DatagramPacket(receivedPacketNum,receivedPacketNum.length,senderAdd,2011);
			localSocket.send(sendPacket);
			
			sortPackets();
			setDataBytes(fuseChunks());
			
//			byte[] receivedSize = ByteBuffer.allocate(4).putInt(this.getDatabytes().length).array();
//			
//			DatagramPacket sendPacket = new DatagramPacket(receivedSize,receivedSize.length,senderAddress,2011);
//			localSocket.send(sendPacket);
			
			fileMaker(getDatabytes());
			
			return null;
		}@Override
		protected void done() {
			// TODO Auto-generated method stub
			super.done();
			System.out.println("Done in " + counter);
		}
	};
	
	public PacketFuser(){
		packets = new ArrayList<FilePacket>();
		try {
			localSocket = new DatagramSocket(4000);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sortPackets(){
		ArrayList<FilePacket> temp = new ArrayList<FilePacket>();
		for(int i = 0; i < packets.size(); i++){
			for(int j = 0; j < packets.size(); j++){
				if(packets.get(j).getHeader().getPcktNumInt() == i)
					temp.add(packets.get(j));
			}
		}
		this.packets = temp;
	}
	
	public void receiveFile() throws Exception{
		this.receiver = new PacketReceiver();
		
		worker.execute();
		
		while(!worker.isDone()){
			Thread.sleep(1000);
			System.out.println("Receiving...");
		}
		
		this.receiver.getClientSocket().close();
		localSocket.close();
	}
	
	public byte[] fuseChunks() throws Exception{
		byte[] allChunks = null;
		System.out.println("Fusing Chunks");
//		for(int i = 0; i < packets.size(); i++){
//			allChunks = allChunks + packets.get(i).getChunk();
//			System.out.println("All Chunks Length: " + allChunks.length());
//		}
		
		int temp = 0;
		for(int i = 0; i < this.packets.size(); i++){
			temp += this.packets.get(i).getChunk().length;
		}
		
		allChunks = new byte[temp];
		
		int j = 0;
		for(int i = 0; i < this.packets.size(); i++){
			for(int k = 0; k < this.packets.get(i).getChunk().length; k++){
				allChunks[j] = this.packets.get(i).getChunk()[k];
				j++;
			}
		}
		
		return allChunks;
	}
	
	public void fileMaker(byte[] fileBytes) throws Exception{
		FileOutputStream fos = new FileOutputStream(new File("C:/Users/Jarl Brent/NETWORK SAMPLES/" + this.packets.get(0).getHeader().getFileName()));
		
		
		System.out.println("Writing File " + this.packets.get(0).getHeader().getFileName());
		fos.write(fileBytes);
		
		fos.close();
	}
	
	public void setPackets(ArrayList<FilePacket> packets){
		this.packets = packets;
	}
	
	public ArrayList<FilePacket> getPackets(){
		return this.packets;
	}
	
	public void setDataBytes(byte[] dataBytes){
		this.dataBytes = dataBytes;
	}
	
	public byte[] getDatabytes(){
		return this.dataBytes;
	}
	
	public class TimedSender extends TimerTask {
		protected DatagramSocket localSocket;
		protected InetAddress senderAdd;
		
		public TimedSender(InetAddress senderAdd, DatagramSocket localSocket){
			this.senderAdd = senderAdd;
			this.localSocket = localSocket;
		}
		
		@Override
		public void run() {
			byte[] receivedPacketNum = ByteBuffer.allocate(4).putInt(-1).array();
					
			if(this.senderAdd != null && patience > 0){
				DatagramPacket sendPacket = new DatagramPacket(receivedPacketNum,receivedPacketNum.length,senderAdd,2011);
				try {
					localSocket.send(sendPacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				worker.cancel(true);
			}
			patience--;
		}
	}
}
