package client;

import java.awt.CardLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class ApplicationClient extends JFrame implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel current;
	protected JPanel audioVideo, image;
	protected JButton playOrStop, next, prev, back, faster, slower, audioVideoButton, imageButton;
	protected JButton videoNext, videoPrev, videoPlayOrStop;
	protected JLabel screen, videoScreen, vidTxt;
	protected String proxy;
	protected boolean isImgPane;
	
	protected DatagramSocket clientSocket = new DatagramSocket(1111);
	
	CardLayout cl = new CardLayout();
	
	SwingWorker<String,Void> worker = new SwingWorker<String,Void>(){

		@Override
		protected String doInBackground() throws Exception {
			String modifiedSentence = null;
			int i = 0;
			while(true){
				byte[] receiveData = new byte[1024];
				
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				System.out.println(i++);
				clientSocket.receive(receivePacket);
				modifiedSentence = new String(receivePacket.getData());
				
				if(isImgPane)
					screen.setText(modifiedSentence);
				else{
					vidTxt.setText(modifiedSentence);
				}
				System.out.println("FROM SERVER:" + modifiedSentence);
			}
		}
		
		protected void done(){
			System.out.println("It went out.");
		}
		
	};
	
	public ApplicationClient() throws SocketException{
		this.setBounds(0, 0, 480, 854);
		this.setLayout(null);
		
		this.isImgPane = true;
		
		this.audioVideo = new JPanel();
		this.audioVideo.setLayout(null);
		this.audioVideo.setBounds(0, 0, 480, 96*4);
		
		this.image = new JPanel();
		this.image.setLayout(null);
		this.image.setBounds(0, 0, 480, 96*4);
		
		this.current = new JPanel();
		this.current.setLayout(cl);
		this.current.setBounds(0, 0, 480, 96*4);
		
		this.current.add("Audio/Video", this.audioVideo);
		this.current.add("Image", this.image);
		
		this.playOrStop = new JButton("Play");
		this.playOrStop.setBounds(160, 96*2, 160, 96);
		this.playOrStop.addMouseListener(this);
		
		this.next = new JButton("Next");
		this.next.setBounds(160, 96, 160, 96);
		this.next.addMouseListener(this);
		
		this.prev = new JButton("Prev");
		this.prev.setBounds(160, 96*3, 160, 96);
		this.prev.addMouseListener(this);
		
		this.faster = new JButton("Faster");
		this.faster.setBounds(160*2, 96*2, 160, 96);
		this.faster.addMouseListener(this);
		
		this.slower = new JButton("Slower");
		this.slower.setBounds(0, 96*2, 160, 96);
		this.slower.addMouseListener(this);
		
		this.videoNext = new JButton("Next");
		this.videoNext.setBounds(160*2, 96*2, 160, 96);
		this.videoNext.addMouseListener(this);
		
		this.videoPlayOrStop = new JButton("Play");
		this.videoPlayOrStop.setBounds(160, 96*2, 160, 96);
		this.videoPlayOrStop.addMouseListener(this);
		
		this.videoPrev = new JButton("Prev");
		this.videoPrev.setBounds(0, 96*2, 160, 96);
		this.videoPrev.addMouseListener(this);
		
		this.videoScreen = new JLabel();
		this.videoScreen.setIcon(new ImageIcon(this.getClass().getResource("audioGIF.gif")));
		this.videoScreen.setBounds(0, 0, 160*3, 96*2);
		
		this.vidTxt = new JLabel();
		this.vidTxt.setBounds(0, 96*3, 160*3, 96);
		
		this.audioVideo.add(this.videoNext);
		this.audioVideo.add(this.videoPlayOrStop);
		this.audioVideo.add(this.videoPrev);
		this.audioVideo.add(this.videoScreen);
		this.audioVideo.add(this.vidTxt);
		
		this.imageButton = new JButton("Image");
		this.imageButton.setBounds(0, 96*5, 160, 96);
		this.imageButton.addMouseListener(this);
		
		this.audioVideoButton = new JButton("Audio/Video");
		this.audioVideoButton.setBounds(160*2, 96*5, 160, 96);
		this.audioVideoButton.addMouseListener(this);
		
		this.back = new JButton("Back");
		this.back.setBounds(160, 96*4, 160, 96);
		this.back.addMouseListener(this);
		
		this.screen = new JLabel();
		this.screen.setBounds(0, 0, 160*3, 96);
		
		this.image.add(this.playOrStop);
		this.image.add(this.next);
		this.image.add(this.prev);
		this.image.add(this.faster);
		this.image.add(this.slower);
		this.add(this.back);
		this.add(this.imageButton);
		this.add(this.audioVideoButton);
		this.image.add(this.screen);
		
		this.add(this.current);
		
		cl.show(this.current, "Image");
		
		this.vidTxt.setVisible(true);
		this.videoScreen.setVisible(true);
		this.playOrStop.setVisible(true);
		this.next.setVisible(true);
		this.prev.setVisible(true);
		this.faster.setVisible(true);
		this.slower.setVisible(true);
		this.back.setVisible(true);
		this.imageButton.setVisible(true);
		this.audioVideoButton.setVisible(true);
		this.screen.setVisible(true);
		this.setVisible(true);
		
		worker.execute();
		
	}
	
	public String getValue(MouseEvent button){
		if(button.getSource() == this.playOrStop)
			return "p";
		else if(button.getSource() == this.next)
			return "n";
		else if(button.getSource() == this.prev)
			return "b";
		else if(button.getSource() == this.faster)
			return "f";
		else if(button.getSource() == this.slower)
			return "s";
		else if(button.getSource() == this.back)
			return "m";
		else if(button.getSource() == this.audioVideoButton)
			return "v";
		else if(button.getSource() == this.imageButton)
			return "i";
		else if(button.getSource() == this.videoNext)
			return "l";
		else if(button.getSource() == this.videoPlayOrStop)
			return "k";
		else if(button.getSource() == this.videoPrev)
			return "j";
		else
			return "q";
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	
	public void sendCmd() throws Exception{
		
//		DatagramSocket clientSocket = new DatagramSocket(1111);
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData = new byte[1];
		String cmd;
		
		cmd = this.getProxy();
		
		System.out.println(cmd);
		sendData = cmd.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 2222);
		clientSocket.send(sendPacket);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		proxy = getValue(e);
		try {
			this.sendCmd();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(e.getSource().equals(this.audioVideoButton)){
			this.isImgPane = false;
			cl.show(this.current, "Audio/Video");
		}
		if(e.getSource().equals(this.imageButton)){
			this.isImgPane = true;
			cl.show(this.current, "Image");
		}
	}
}