package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*; 
import java.net.*; 

import javax.swing.Timer;
class UDPServer {
	protected static byte[] receiveData = new byte[1];
	protected static byte[] sendData = new byte[100];
	public static final char PLAY_OR_STOP = 'p', SPD_UP = 'f', SPD_DWN = 's', NEXT = 'n', PREV = 'b', EXIT = 'x', AUDIO = 'a', VIDEO = 'v', IMAGE = 'i', BACK = 'm';
	
	
	
	public static String receiveMessage(DatagramSocket serverSocket, DatagramPacket receivePacket) throws Exception{
		serverSocket.receive(receivePacket);
		return new String(receivePacket.getData());
	}
	
	
	
	public static void main(String args[]) throws Exception
	{
		ApplicationServer as = new ApplicationServer();
		
//		class TimerListener implements ActionListener {
//			DatagramPacket receivePacket;
//			InetAddress IPAddress;
//			DatagramSocket serverSocket;
//			
//			
//			
//			public TimerListener(DatagramPacket receivePacket,
//					InetAddress iPAddress, DatagramSocket serverSocket) {
//				this.receivePacket = receivePacket;
//				IPAddress = iPAddress;
//				this.serverSocket = serverSocket;
//			}
//
//
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				int port = receivePacket.getPort();
//				String capitalizedSentence = as.getIconName();
//				sendData = capitalizedSentence.getBytes();
//				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//				try {
//					serverSocket.send(sendPacket);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//		}
//		Timer clock;
		
		DatagramSocket serverSocket = new DatagramSocket(9876);
//		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//		InetAddress IPAddress = receivePacket.getAddress();
//		clock = new Timer(100,new TimerListener(receivePacket, IPAddress, serverSocket));
//		clock.start();
		
		
		while(true)
		{
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			String sentence = receiveMessage(serverSocket,receivePacket);
			char cmd = sentence.charAt(0);
			as.refresh(cmd);
			System.out.println("RECEIVED: " + sentence);
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			String capitalizedSentence = as.getIconName();
			sendData = capitalizedSentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
	}
}