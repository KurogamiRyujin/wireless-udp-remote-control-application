package server;

import java.net.*; 

import javax.swing.SwingWorker;
class UDPServer {
	protected static byte[] receiveData = new byte[1];
	protected static byte[] sendData = new byte[1];
	protected static String sentence;
	protected static DatagramSocket serverSocket;
	public static final char PLAY_OR_STOP = 'p', SPD_UP = 'f', SPD_DWN = 's', NEXT = 'n', PREV = 'b', EXIT = 'x', AUDIOVIDEO = 'v', IMAGE = 'i', BACK = 'm', AVPLAY = 'k', AVNEXT = 'l', AVPREV = 'j', SENDING = 'z', SEND = 'x', CHOOSE = 'c';
	
	public static String receiveMessage(DatagramSocket serverSocket, DatagramPacket receivePacket) throws Exception{
		serverSocket.receive(receivePacket);
		return new String(receivePacket.getData());
	}
	
	static ApplicationServer as = new ApplicationServer();
	
	static SwingWorker<String,Void> worker = new SwingWorker<String,Void>(){

		@Override
		protected String doInBackground() throws Exception {
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
		
	};
	
	public static void main(String args[]) throws Exception
	{
		
		serverSocket = new DatagramSocket(2222);
		as.setServerSocket(serverSocket);
		worker.execute();
	}
}