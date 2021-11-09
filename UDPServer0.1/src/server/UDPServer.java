package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*; 
import java.net.*; 

import javax.swing.SwingWorker;
import javax.swing.Timer;
class UDPServer {
	protected static byte[] receiveData/* = new byte[1]*/;
//	protected static byte[] sendData/* = new byte[1]*/;
//	protected static String sentence;
//	protected static char cmd;
	protected static DatagramSocket serverSocket;
	public static final char PLAY_OR_STOP = 'p', SPD_UP = 'f', SPD_DWN = 's', NEXT = 'n', PREV = 'b', EXIT = 'x', AUDIO = 'a', VIDEO = 'v', IMAGE = 'i', BACK = 'm';
	
	public static String receiveMessage(DatagramSocket serverSocket, DatagramPacket receivePacket) throws Exception{
		serverSocket.receive(receivePacket);
		return new String(receivePacket.getData());
	}
	
//	public static class Sender extends Thread {
////		protected String capitalizedSentence;
//		protected ApplicationServer as;
////		protected DatagramPacket receivePacket;
//		protected long time = 500;
//		
//		public Sender(ApplicationServer as/*, String capitalizedSentence/*, DatagramPacket receivePacket*/){
////			capitalizedSentence = capitalizedSentence;
////			this.receivePacket = receivePacket;
//			this.as = as;
//		}
//		
//		@Override
//		public void run() {
//			while(true){
//				receiveData = new byte[1];
//				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//				
//				try {
//					serverSocket.receive(receivePacket);
//				} catch (IOException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				}
//				
//				
//				String sentence = new String(receivePacket.getData());
//				char cmd = sentence.charAt(0);
//				as.refresh(cmd);
//				
//				capitalizedSentence = as.getIconName();
//				
//				InetAddress IPAddress = receivePacket.getAddress();
//				
//				int port = receivePacket.getPort();
//				sendData = new byte[1];
//				sendData = capitalizedSentence.getBytes();
//				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//				try {
//					System.out.println("Thread: " + capitalizedSentence);
//					serverSocket.send(sendPacket);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				try {
//					Thread.sleep(this.time);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		
//		public void setData(String data){
//			capitalizedSentence = data;
//		}
//		
//	}
	
//	public static class ThreadListener implements ActionListener{
//		protected String iconName;
//		protected DatagramPacket receivePacket;
//		
//		public void actionPerformed(ActionEvent e){
//			Sender sender = new Sender(this.iconName,this.receivePacket);
//			sender.start();
//			try {
//				sender.join();
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//		
//		public void setIconName(String iconName){
//			this.iconName = iconName;
//		}
//		
//		public void setReceivePacket(DatagramPacket receivePacket){
//			this.receivePacket = receivePacket;
//		}
//	}
	
	public static void main(String args[]) throws Exception
	{
		ApplicationServer as = new ApplicationServer();
		serverSocket = new DatagramSocket(9876);
		receiveData = new byte[1];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//		ThreadListener tl = new ThreadListener();
//		Timer clock = new Timer(100,tl);
		
		receiveData = new byte[1];
//		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		serverSocket.receive(receivePacket);
		
		SwingWorker<String,Void> worker = new SwingWorker<String,Void>(){

			@Override
			protected String doInBackground() throws Exception {
				while(true){
					String sentence = receiveMessage(serverSocket,receivePacket);
					char cmd = sentence.charAt(0);
					as.refresh(cmd);
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		};
		worker.execute();
		
		while(true){
			String capitalizedSentence = as.getIconName();
			System.out.println(capitalizedSentence);
			InetAddress IPAddress = receivePacket.getAddress();
			
			int port = receivePacket.getPort();
			byte[] sendData = new byte[1];
			sendData = capitalizedSentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
//		Thread receive = new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
//		receive.start();
//		while(true)
//		{
//			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//			tl.setReceivePacket(receivePacket);
//			Thread receive = new Thread(new Runnable(){
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					while(true){
//						try {
//							sentence = receiveMessage(serverSocket,receivePacket);
//							cmd = sentence.charAt(0);
//							as.refresh(cmd/*,sentence,receivePacket,serverSocket*/);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
//				
//			});
//			String sentence = receiveMessage(serverSocket,receivePacket);
//			char cmd = sentence.charAt(0);
//			as.refresh(cmd);
			
//			receive.start();
//			receive.join();
//			cmd = sentence.charAt(0);
//			as.refresh(cmd/*,sentence,receivePacket,serverSocket*/);
//			capitalizedSentence = as.getIconName();
			
			
//			tl.setIconName(capitalizedSentence);
//			if(!clock.isRunning())
//				clock.start();
//			System.out.println("Icon: " + capitalizedSentence);
//			
//			Sender sender = new Sender(as/*,capitalizedSentence/*,receivePacket*/);
//			System.out.println("SERVER RECEIVED: " + sentence);
			
//			sender.setData(capitalizedSentence);
//			if(!sender.isAlive()){
//				sender.start();
//			}
			
			
//			System.out.println("RECEIVED: " + sentence);
//			
//			InetAddress IPAddress = receivePacket.getAddress();
//			
//			int port = receivePacket.getPort();
//			sendData = capitalizedSentence.getBytes();
//			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//			serverSocket.send(sendPacket);
//		}
	}
}