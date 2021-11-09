package server;

import java.io.*; 
import java.net.*; 
class UDPServer {
	protected static byte[] receiveData = new byte[1];
	protected static byte[] sendData = new byte[1];
	protected static String sentence;
	protected static DatagramSocket serverSocket;
	public static final char PLAY_OR_STOP = 'p', SPD_UP = 'f', SPD_DWN = 's', NEXT = 'n', PREV = 'b', EXIT = 'x', AUDIO = 'a', VIDEO = 'v', IMAGE = 'i', BACK = 'm';
	
	public static String receiveMessage(DatagramSocket serverSocket, DatagramPacket receivePacket) throws Exception{
		serverSocket.receive(receivePacket);
		return new String(receivePacket.getData());
	}
	
	public static void main(String args[]) throws Exception
	{
		ApplicationServer as = new ApplicationServer();
		serverSocket = new DatagramSocket(9876);
		as.setServerSocket(serverSocket);
		while(true)
		{
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			as.setReceivePacket(receivePacket);
			
			sentence = receiveMessage(serverSocket,receivePacket);
			
			if(as.getIPAddress() == null && as.getPort() == 0){
				as.setIPAddress(receivePacket.getAddress());
				as.setPort(receivePacket.getPort());
			}
			
			char cmd = sentence.charAt(0);
			as.refresh(cmd);
		}
	}
}