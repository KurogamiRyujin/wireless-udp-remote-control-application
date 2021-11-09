package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingWorker;

public class FileSender {
	protected final static int timeCycle = 3000;
	protected final static int PROBABILITY_LOSS = 0;
	protected long current;
	protected static int packetSent = 0;
	protected static int chunkSize = 0;
	
	protected static Queue<FilePacket> pcktQTemp = new LinkedList<FilePacket>();
	protected static Queue<FilePacket> pcktsToResend = new LinkedList<FilePacket>();
	protected static ArrayList<FilePacket> temp = new ArrayList<FilePacket>();
	protected static Queue<Integer> num = new LinkedList<Integer>();
	
	protected DatagramSocket localSocket;
	protected Timer timer;
	
	private static synchronized FilePacket getPacket(){
		FilePacket temp = null;
		
		if(!pcktQTemp.isEmpty()){
			temp = pcktQTemp.poll();
			packetSent++;
		}
		
		return temp;
	}
	
	public void sendFile(PacketsHolder holder, DatagramSocket clientSocket, InetAddress IPAddress) throws Exception{
		temp = new ArrayList<FilePacket>();
		Queue<FilePacket> tempQ = holder.getPcktQ();
		
		while(!tempQ.isEmpty()){
			FilePacket tempo = tempQ.poll();
			
			temp.add(tempo);
			pcktQTemp.offer(tempo);
			pcktsToResend.offer(tempo);
			System.out.println(temp.size());
		}
		
		if(temp.isEmpty()) System.out.println("Biggest fuck ever");
		if(pcktQTemp.isEmpty()) System.out.println("FUck dis 2");
//		int[] pcktNumAcks;
//		int pcktNumGets = 0;
//		int fileSize = holder.getPcktQ().peek().getHeader().getFileSizeInt();
		
		DatagramSocket sizeSendSocket = new DatagramSocket(2010);
//		byte[] sendPacketSize = new byte[4];
		localSocket = new DatagramSocket(2011);
		boolean toResend = false;
		
		do{
			this.setCurrent(System.currentTimeMillis());
			
			if(pcktQTemp.isEmpty()) System.out.println("FUck dis");
		Thread sender1 = new Thread(new Runnable(){
			public void run(){
				while(!pcktQTemp.isEmpty() && getCurrent() <= System.currentTimeMillis()+timeCycle){
//					System.out.println("Holder packet: " + holder.getPcktQ().peek().getChunk().length);
//					chunkSize = holder.getPcktQ().peek().getChunk().length;
					byte[] sendData = null;
					try {
						sendData = getPacket().mergeProp();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
						
//						int temp = holder.getPcktQ().peek().getChunk().length+holder.getPcktQ().peek().getHeader().getFileName().length()
//								+String.valueOf(holder.getPcktQ().peek().getHeader().getFileSize()).length()+String.valueOf(holder.getPcktQ().size()).length();
//						
//						System.out.println("Sent Packet Size: " + temp);
						
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Random rand = new Random();
					
					if(rand.nextInt(100) < FileSender.PROBABILITY_LOSS){
						System.out.println("Packet Lost");
					}
					else if(sendData != null){
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
						System.out.println("Send Data Length: " + sendData.length);
						int temp = sendData.length;
						
						byte[] sendPacketSize = ByteBuffer.allocate(4).putInt(temp).array();
						
						DatagramPacket nPacketSize = new DatagramPacket(sendPacketSize,sendPacketSize.length,IPAddress,4000);
						
						try {
							sizeSendSocket.send(nPacketSize);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						try {
							clientSocket.send(sendPacket);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						System.out.println("Packet Sent: " + packetSent);
					}
					
					
				}
			}
			
		});
		
		Thread sender2 = new Thread(new Runnable(){
			public void run(){
				while(!pcktQTemp.isEmpty() && getCurrent() <= System.currentTimeMillis()+timeCycle){
//					System.out.println("Holder packet: " + holder.getPcktQ().peek().getChunk().length);
//					chunkSize = holder.getPcktQ().peek().getChunk().length;
					byte[] sendData = null;
					try {
						sendData = getPacket().mergeProp();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
						
//						int temp = holder.getPcktQ().peek().getChunk().length+holder.getPcktQ().peek().getHeader().getFileName().length()
//								+String.valueOf(holder.getPcktQ().peek().getHeader().getFileSize()).length()+String.valueOf(holder.getPcktQ().size()).length();
//						
//						System.out.println("Sent Packet Size: " + temp);
						
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Random rand = new Random();
					
					if(rand.nextInt(100) < FileSender.PROBABILITY_LOSS){
						System.out.println("Packet Lost");
					}
					else if(sendData != null){
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
						System.out.println("Send Data Length: " + sendData.length);
						int temp = sendData.length;
						
						byte[] sendPacketSize = ByteBuffer.allocate(4).putInt(temp).array();
						
						DatagramPacket nPacketSize = new DatagramPacket(sendPacketSize,sendPacketSize.length,IPAddress,4000);
						
						try {
							sizeSendSocket.send(nPacketSize);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						try {
							clientSocket.send(sendPacket);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						System.out.println("Packet Sent: " + packetSent);
					}
					
					
				}
			}
		});
		
		Thread sender3 = new Thread(new Runnable(){
			public void run(){
				while(!pcktQTemp.isEmpty() && getCurrent() <= System.currentTimeMillis()+timeCycle){
//					System.out.println("Holder packet: " + holder.getPcktQ().peek().getChunk().length);
//					chunkSize = holder.getPcktQ().peek().getChunk().length;
					byte[] sendData = null;
					try {
						sendData = getPacket().mergeProp();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
						
//						int temp = holder.getPcktQ().peek().getChunk().length+holder.getPcktQ().peek().getHeader().getFileName().length()
//								+String.valueOf(holder.getPcktQ().peek().getHeader().getFileSize()).length()+String.valueOf(holder.getPcktQ().size()).length();
//						
//						System.out.println("Sent Packet Size: " + temp);
						
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Random rand = new Random();
					
					if(rand.nextInt(100) < FileSender.PROBABILITY_LOSS){
						System.out.println("Packet Lost");
					}
					else if(sendData != null){
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
						System.out.println("Send Data Length: " + sendData.length);
						int temp = sendData.length;
						
						byte[] sendPacketSize = ByteBuffer.allocate(4).putInt(temp).array();
						
						DatagramPacket nPacketSize = new DatagramPacket(sendPacketSize,sendPacketSize.length,IPAddress,4000);
						
						try {
							sizeSendSocket.send(nPacketSize);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						try {
							clientSocket.send(sendPacket);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						System.out.println("Packet Sent: " + packetSent);
					}
					
					
				}
			}
		});
		
		Thread sender4 = new Thread(new Runnable(){
			public void run(){
				while(!pcktQTemp.isEmpty() && getCurrent() <= System.currentTimeMillis()+timeCycle){
//					System.out.println("Holder packet: " + holder.getPcktQ().peek().getChunk().length);
//					chunkSize = holder.getPcktQ().peek().getChunk().length;
					byte[] sendData = null;
					try {
						sendData = getPacket().mergeProp();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
						
//						int temp = holder.getPcktQ().peek().getChunk().length+holder.getPcktQ().peek().getHeader().getFileName().length()
//								+String.valueOf(holder.getPcktQ().peek().getHeader().getFileSize()).length()+String.valueOf(holder.getPcktQ().size()).length();
//						
//						System.out.println("Sent Packet Size: " + temp);
						
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Random rand = new Random();
					
					if(rand.nextInt(100) < FileSender.PROBABILITY_LOSS){
						System.out.println("Packet Lost");
					}
					else if(sendData != null){
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
						System.out.println("Send Data Length: " + sendData.length);
						int temp = sendData.length;
						
						byte[] sendPacketSize = ByteBuffer.allocate(4).putInt(temp).array();
						
						DatagramPacket nPacketSize = new DatagramPacket(sendPacketSize,sendPacketSize.length,IPAddress,4000);
						
						try {
							sizeSendSocket.send(nPacketSize);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						try {
							clientSocket.send(sendPacket);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						System.out.println("Packet Sent: " + packetSent);
					}
					
					
				}
			}
		});
		
		Thread sender5 = new Thread(new Runnable(){
			public void run(){
				while(!pcktQTemp.isEmpty() && getCurrent() <= System.currentTimeMillis()+timeCycle){
//					System.out.println("Holder packet: " + holder.getPcktQ().peek().getChunk().length);
//					chunkSize = holder.getPcktQ().peek().getChunk().length;
					byte[] sendData = null;
					try {
						sendData = getPacket().mergeProp();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
						
//						int temp = holder.getPcktQ().peek().getChunk().length+holder.getPcktQ().peek().getHeader().getFileName().length()
//								+String.valueOf(holder.getPcktQ().peek().getHeader().getFileSize()).length()+String.valueOf(holder.getPcktQ().size()).length();
//						
//						System.out.println("Sent Packet Size: " + temp);
						
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					Random rand = new Random();
					
					if(rand.nextInt(100) < FileSender.PROBABILITY_LOSS){
						System.out.println("Packet Lost");
					}
					else if(sendData != null){
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
						System.out.println("Send Data Length: " + sendData.length);
						int temp = sendData.length;
						
						byte[] sendPacketSize = ByteBuffer.allocate(4).putInt(temp).array();
						
						DatagramPacket nPacketSize = new DatagramPacket(sendPacketSize,sendPacketSize.length,IPAddress,4000);
						
						try {
							sizeSendSocket.send(nPacketSize);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						try {
							clientSocket.send(sendPacket);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						System.out.println("Packet Sent: " + packetSent);
					}
					
					
				}
			}
		});
		
		SwingWorker<Void,Void> worker = new SwingWorker<Void,Void>(){

			@Override
			protected Void doInBackground() throws Exception {
				// TODO Auto-generated method stub
				boolean toGet = true;
				
				while(toGet){
					timer = new Timer();
					timer.schedule(new AckChecker(this), 10000);
					
					System.out.println("Sending...");
					
					byte[] receiveAck = new byte[4];
					
					DatagramPacket acknowledgement = new DatagramPacket(receiveAck,receiveAck.length);
					try {
						localSocket.receive(acknowledgement);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					timer.cancel();
					System.out.println("Packet Num Acknowledgement: " + java.nio.ByteBuffer.wrap(receiveAck).getInt());
					
					if(java.nio.ByteBuffer.wrap(receiveAck).getInt() == -1){
						toGet = false;
					}
					else
						pcktsToResend(java.nio.ByteBuffer.wrap(receiveAck).getInt());
				}
				
				return null;
			}
		};
		
		sender1.start();
		sender2.start();
		sender3.start();
		sender4.start();
		sender5.start();
		
		sender1.join();
		sender2.join();
		sender3.join();
		sender4.join();
		sender5.join();
		
		worker.execute();
		
		while(sender1.isAlive() || sender2.isAlive() || sender3.isAlive() || sender4.isAlive() || sender5.isAlive() || !worker.isDone()){
			Thread.sleep(1000);
			if(sender1.isAlive() || sender2.isAlive() || sender3.isAlive() || sender4.isAlive() || sender5.isAlive())
				System.out.println("Sending...");
//			
			System.out.println(temp.size() + " VS " + pcktQTemp.size());
//			byte[] receiveAck = new byte[4];
//			
//			DatagramPacket acknowledgement = new DatagramPacket(receiveAck,receiveAck.length);
//			localSocket.receive(acknowledgement);
//			System.out.println("Packet Num Acknowledgement: " + java.nio.ByteBuffer.wrap(receiveAck).getInt());
//			
//			pcktsToResend = this.pcktsToResend(pcktsToResend, java.nio.ByteBuffer.wrap(receiveAck).getInt());
		}
		
//		while(!num.isEmpty()){
//			pcktsToResend.remove(temp.get(num.poll()));
//		}
//		
//		pcktQTemp = pcktsToResend;
		worker.cancel(true);
		pcktQTemp = new LinkedList<FilePacket>();
		
		for(int i = 0; i < temp.size(); i++){
			pcktQTemp.offer(temp.get(i));
		}
		
		if(!pcktQTemp.isEmpty()){
			System.out.println("Went to if");
			toResend = true;
		}
		else {
			System.out.println("Went to Else");
			sizeSendSocket.close();
			localSocket.close();
			toResend = false;
		}
		} while(toResend);
	}
	
	public void pcktsToResend(int pcktNum){
		for(int i = 0; i < temp.size(); i++){
			if(temp.get(i).getHeader().getPcktNumInt() == pcktNum){
				temp.remove(i);
				return;
			}
		}
	}
	
	public void setCurrent(long current){
		this.current = current;
	}
	
	public long getCurrent(){
		return this.current;
	}
	
	public class AckChecker extends TimerTask {
		SwingWorker<Void,Void> worker;
		
		public AckChecker(SwingWorker<Void,Void> worker){
			this.worker = worker;
		}
		
		public void run(){
			temp.clear();
			this.worker.cancel(true);
		}
	}
}
