package client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
	protected JPanel audioVideo, image, bg, sending;
	protected JButton playOrStop, next, prev, back, faster, slower, send, choose, audioVideoButton, imageButton, sendingButton;
	protected JButton videoNext, videoPrev, videoPlayOrStop;
	protected JLabel screen, videoScreen, vidTxt;
	protected String proxy, curr;
	protected boolean isImgPane, isPlaying, Iplay;
	
	protected final JFileChooser filechooser = new JFileChooser();
//	protected FileSender fileSender;
	protected File temp;
	
	protected DatagramSocket clientSocket = new DatagramSocket(1111);
	
	CardLayout cl = new CardLayout();
	
	
	protected JLabel img;
	protected static ByteArrayInputStream receivedBytes;
	protected static String allData = "";
	
	protected DatagramSocket clientSocket2 = new DatagramSocket(1112);
	
	SwingWorker<String,Void> worker1 = new SwingWorker<String,Void>(){

		@Override
		protected String doInBackground() throws Exception {
			String modifiedSentence = null;
			byte[] allOfIt = null;
			long byteSize = 0;
			int numPckt = 0;
			while(true){
				byte[] receiveData = new byte[1500];
				
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket2.receive(receivePacket);
				byteSize += receivePacket.getLength();
				modifiedSentence = new String(receivePacket.getData(),"ISO-8859-1");
				if(modifiedSentence != null){
					if(modifiedSentence.contains("EOF")){
						byteSize = 0;
						numPckt = 0;
						System.out.println("Went in");
						byteConversion(allOfIt);
						allOfIt = null;
						modifiedSentence = null;
					}
					else {
						byte[] temp = allOfIt;
						allOfIt = new byte[(int)byteSize];
						for(int i = 0; i < byteSize; i++){
							if(temp != null){
								if(i < temp.length)
									allOfIt[i] = temp[i];
								else {
									allOfIt[i] = receivePacket.getData()[i-temp.length];
									numPckt++;
									System.out.println("num Packets: "+numPckt);
								}
							}
							else{
								allOfIt[i] = receivePacket.getData()[i];
								numPckt++;
								System.out.println("num Packets: "+numPckt);
							}
						}
					}
				}
					
			}
		}
		protected void done(){
			
			System.out.println("NO FILE");
		}
		
	};
	
	
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
				modifiedSentence = new String(receivePacket.getData(),"ISO-8859-1");
				if(curr != modifiedSentence){
					if(isImgPane){
						screen.setText(modifiedSentence);
					}
					else{
						vidTxt.setText(modifiedSentence);
					}
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
		Iplay = false;
		curr = null;
		
		this.sending = new JPanel();
		this.sending.setLayout(null);
		this.sending.setBounds(0, 0, 480, 96*4);
		this.sending.setBackground(Color.black);
		
		this.audioVideo = new JPanel();
		this.audioVideo.setLayout(null);
		this.audioVideo.setBounds(0, 0, 480, 96*4);
		this.audioVideo.setBackground(Color.black);
		
		this.image = new JPanel();
		this.image.setLayout(null);
		this.image.setBounds(0, 0, 480, 96*4);
		this.image.setBackground(Color.black);
		
		this.current = new JPanel();
		this.current.setLayout(cl);
		this.current.setBounds(0, 0, 480, 96*4);
		this.current.setBackground(Color.black);
		
		this.current.add("Sending", this.sending);
		this.current.add("Audio/Video", this.audioVideo);
		this.current.add("Image", this.image);
		
		this.sendingButton = new JButton("Sending Panel");
		this.sendingButton.setLayout(null);
//		this.sendingButton.setBorder(null);
		this.sendingButton.setBounds(160, 96*2, 160, 96);
		this.sendingButton.addMouseListener(this);
		
		this.send = new JButton("Send");
		this.send.setLayout(null);
//		this.send.setBorder(null);
		this.send.setBounds(160*2, 96*2, 160, 96);
		this.send.addMouseListener(this);
		
		this.choose = new JButton("Choose");
		this.choose.setLayout(null);
//		this.choose.setBorder(null);
		this.choose.setBounds(0, 96*2, 160, 96);
		this.choose.addMouseListener(this);
		
		this.sending.add(this.send);
		this.sending.add(this.choose);
		
		this.playOrStop = new JButton(new ImageIcon(this.getClass().getResource("start.png")));
		this.playOrStop.setLayout(null);
		this.playOrStop.setBorder(null);
		this.playOrStop.setBounds(160, 96*2, 160, 96);
		this.playOrStop.addMouseListener(this);
		
		this.next = new JButton(new ImageIcon(this.getClass().getResource("next.png")));
		this.next.setLayout(null);
		this.next.setBorder(null);
		this.next.setBounds(160, 96, 160, 96);
		this.next.addMouseListener(this);
		
		this.prev = new JButton(new ImageIcon(this.getClass().getResource("prev.png")));
		this.prev.setLayout(null);
		this.prev.setBorder(null);
		this.prev.setBounds(160, 96*3, 160, 96);
		this.prev.addMouseListener(this);
		
		this.faster = new JButton(new ImageIcon(this.getClass().getResource("fast.png")));
		this.faster.setLayout(null);
		this.faster.setBorder(null);
		this.faster.setBounds(160*2, 96*2, 160, 96);
		this.faster.addMouseListener(this);
		
		this.slower = new JButton(new ImageIcon(this.getClass().getResource("slow.png")));
		this.slower.setLayout(null);
		this.slower.setBorder(null);
		this.slower.setBounds(0, 96*2, 160, 96);
		this.slower.addMouseListener(this);
		
		this.videoNext = new JButton(new ImageIcon(this.getClass().getResource("avNxt.png")));
		this.videoNext.setBorder(null);
		this.videoNext.setBounds(160*2, 96*2, 160, 96);
		this.videoNext.addMouseListener(this);
		
		this.videoPlayOrStop = new JButton(new ImageIcon(this.getClass().getResource("avPau.png")));
		this.videoPlayOrStop.setBorder(null);
		this.videoPlayOrStop.setBounds(160, 96*2, 160, 96);
		this.videoPlayOrStop.addMouseListener(this);
		
		this.videoPrev = new JButton(new ImageIcon(this.getClass().getResource("avPrv.png")));
		this.videoPrev.setBorder(null);
		this.videoPrev.setBounds(0, 96*2, 160, 96);
		this.videoPrev.addMouseListener(this);
		
		this.videoScreen = new JLabel();
		this.videoScreen.setIcon(new ImageIcon(this.getClass().getResource("audioGIF.gif")));
		this.videoScreen.setBounds(0, 0, 160*3, 96*2);
		
		this.img = new JLabel("afxc");
		this.img.setBounds(160, 30, 160, 160);
		
		this.vidTxt = new JLabel();
		this.vidTxt.setFont(new Font("JD LCD ROUNDED", Font.PLAIN, 40));
		this.vidTxt.setForeground(Color.orange);
		this.vidTxt.setBounds(0, 96*3, 160*3, 96);
		
		this.audioVideo.add(this.videoNext);
		this.audioVideo.add(this.videoPlayOrStop);
		this.audioVideo.add(this.videoPrev);
		this.audioVideo.add(this.videoScreen);
		this.audioVideo.add(this.vidTxt);
		
		this.imageButton = new JButton(new ImageIcon(this.getClass().getResource("img.png")));
		this.imageButton.setBorder(null);
		this.imageButton.setBounds(0, 96, 160, 96);
		this.imageButton.addMouseListener(this);
		
		this.audioVideoButton = new JButton(new ImageIcon(this.getClass().getResource("av.png")));
		this.audioVideoButton.setBorder(null);
		this.audioVideoButton.setBounds(160*2, 96, 160, 96);
		this.audioVideoButton.addMouseListener(this);
		
		this.back = new JButton("Back");
		this.back.setBounds(160, 0, 160, 96);
		this.back.addMouseListener(this);
		
		this.screen = new JLabel();
		this.screen.setFont(new Font("JD LCD ROUNDED", Font.PLAIN, 40));
		this.screen.setForeground(Color.cyan);
		this.screen.setBounds(0, 0, 160*3, 96);
		
		this.bg = new JPanel();
		this.bg.setLayout(null);
		this.bg.setBounds(0, 96*4, 160*3, 96*4);
		this.bg.setBackground(Color.black);
		
		this.bg.add(this.img);
		this.bg.add(this.imageButton);
		this.bg.add(this.audioVideoButton);
		this.bg.add(this.sendingButton);
		
		this.image.add(this.playOrStop);
		this.image.add(this.next);
		this.image.add(this.prev);
		this.image.add(this.faster);
		this.image.add(this.slower);
		this.add(this.bg);
		this.image.add(this.screen);
		
		this.add(this.current);
		
		cl.show(this.current, "Sending");
		
		this.setBackground(Color.black);
		
		this.send.setVisible(true);
		this.choose.setVisible(true);
		this.sendingButton.setVisible(true);
		this.sending.setVisible(true);
		this.img.setVisible(true);
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
		this.isPlaying = true;
		worker1.execute();
		worker.execute();
		
//		fileSender = new FileSender();
		
	}
	
	public static ImageIcon resIcon(ImageIcon icon, int size) {

		Image img = icon.getImage();
		Image resImg = img.getScaledInstance(size, size,
				java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(resImg);
		return newIcon;
	}
	
	public void byteConversion(byte[] input) throws Exception{
		byte[] bytes = input;
		
//		ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);
//		BufferedImage image = ImageIO.read(inStream);
		
		Image image = Toolkit.getDefaultToolkit().createImage(bytes);
		
		
//		
//		 FileOutputStream fileOuputStream = new FileOutputStream("C:\\Users\\Richmond Manga\\Desktop\\b\\testing2.png"); 
//	    fileOuputStream.write(bytes);
//	    fileOuputStream.close();
		
//		DataInputStream in = new DataInputStream(inStream);
//		in.readFully(bytes);
//		in.close();
		this.img.setIcon(resIcon(new ImageIcon(image),160));
//		System.out.println(image.getHeight());
	}
	public String getValue(MouseEvent button){
		if(button.getSource() == this.playOrStop){
			if(Iplay){
				this.playOrStop.setIcon(new ImageIcon(this.getClass().getResource("start.png")));
			}else {
				this.playOrStop.setIcon(new ImageIcon(this.getClass().getResource("pause.png")));
			}
				
			Iplay = !Iplay;
			return "p";
		}
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
		else if(button.getSource() == this.audioVideoButton){
			Iplay = false;
			this.isPlaying = true;
			this.videoScreen.setIcon(new ImageIcon(this.getClass().getResource("audioGIF.gif")));
			this.videoPlayOrStop.setIcon(new ImageIcon(this.getClass().getResource("avPau_on.png")));
			return "v";
		}
		else if(button.getSource() == this.imageButton){
			this.playOrStop.setIcon(new ImageIcon(this.getClass().getResource("start.png")));
			return "i";
		}
		else if(button.getSource() == this.videoNext){
			this.videoScreen.setIcon(new ImageIcon(this.getClass().getResource("audioGIF.gif")));
			this.videoPlayOrStop.setIcon(new ImageIcon(this.getClass().getResource("avPau.png")));
			this.isPlaying = true;
			return "l";
		}
		else if(button.getSource() == this.videoPlayOrStop){
			System.out.println("this.isPlaying: "+this.isPlaying);
			if(this.isPlaying){
				this.videoScreen.setIcon(new ImageIcon(this.getClass().getResource("audioGIFoff.png")));
				this.videoPlayOrStop.setIcon(new ImageIcon(this.getClass().getResource("avPlay_on.png")));
			}else{
				this.videoScreen.setIcon(new ImageIcon(this.getClass().getResource("audioGIF.gif")));
				this.videoPlayOrStop.setIcon(new ImageIcon(this.getClass().getResource("avPau_on.png")));
			}
			this.isPlaying = !this.isPlaying;
			return "k";
		}
		else if(button.getSource() == this.videoPrev){
			this.videoScreen.setIcon(new ImageIcon(this.getClass().getResource("audioGIF.gif")));
			this.videoPlayOrStop.setIcon(new ImageIcon(this.getClass().getResource("avPau_on.png")));
			this.isPlaying = true;
			return "j";
		}
		else if(button.getSource() == this.sendingButton){
			return "z";
		}
		else if(button.getSource() == this.send){
			return "x";
		}
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
//		playOrStop, next, prev, back, faster, slower, audioVideoButton, imageButton;
//		protected JButton videoNext, videoPrev, videoPlayOrStop;
		if(e.getSource() == this.playOrStop){
			if(!Iplay){
				this.playOrStop.setIcon(new ImageIcon(this.getClass().getResource("start_on.png")));
			}else {
				this.playOrStop.setIcon(new ImageIcon(this.getClass().getResource("pause_on.png")));
			}
		}
		else if(e.getSource() == this.next){
			this.next.setIcon(new ImageIcon(this.getClass().getResource("next_on.png")));
		}
		else if(e.getSource() == this.prev){
			this.prev.setIcon(new ImageIcon(this.getClass().getResource("prev_on.png")));
		}
		else if(e.getSource() == this.faster){
			this.faster.setIcon(new ImageIcon(this.getClass().getResource("fast_on.png")));
		}
		else if(e.getSource() == this.slower){
			this.slower.setIcon(new ImageIcon(this.getClass().getResource("slow_on.png")));
		}
		else if(e.getSource() == this.audioVideoButton){
			this.audioVideoButton.setIcon(new ImageIcon(this.getClass().getResource("av_on.png")));
		}
		else if(e.getSource() == this.imageButton){
			this.imageButton.setIcon(new ImageIcon(this.getClass().getResource("img_on.png")));
		}
		else if(e.getSource() == this.videoNext){
			this.videoNext.setIcon(new ImageIcon(this.getClass().getResource("avNxt_on.png")));
		}
		else if(e.getSource() == this.videoPrev){
			this.videoPrev.setIcon(new ImageIcon(this.getClass().getResource("avPrv_on.png")));
		}
		else if(e.getSource() == this.videoPlayOrStop){
			if(!this.isPlaying){
				this.videoPlayOrStop.setIcon(new ImageIcon(this.getClass().getResource("avPlay_on.png")));
			}else{
				this.videoPlayOrStop.setIcon(new ImageIcon(this.getClass().getResource("avPau_on.png")));
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
//		playOrStop, next, prev, back, faster, slower, audioVideoButton, imageButton;
//		protected JButton videoNext, videoPrev, videoPlayOrStop;
		if(e.getSource() == this.playOrStop){
			if(!Iplay){
				this.playOrStop.setIcon(new ImageIcon(this.getClass().getResource("start.png")));
			}else {
				this.playOrStop.setIcon(new ImageIcon(this.getClass().getResource("pause.png")));
			}
		}
		else if(e.getSource() == this.next){
			this.next.setIcon(new ImageIcon(this.getClass().getResource("next.png")));
		}
		else if(e.getSource() == this.prev){
			this.prev.setIcon(new ImageIcon(this.getClass().getResource("prev.png")));
		}
		else if(e.getSource() == this.faster){
			this.faster.setIcon(new ImageIcon(this.getClass().getResource("fast.png")));
		}
		else if(e.getSource() == this.slower){
			this.slower.setIcon(new ImageIcon(this.getClass().getResource("slow.png")));
		}
		else if(e.getSource() == this.audioVideoButton){
			this.audioVideoButton.setIcon(new ImageIcon(this.getClass().getResource("av.png")));
		}
		else if(e.getSource() == this.imageButton){
			this.imageButton.setIcon(new ImageIcon(this.getClass().getResource("img.png")));
		}
		else if(e.getSource() == this.videoNext){
			this.videoNext.setIcon(new ImageIcon(this.getClass().getResource("avNxt.png")));
		}
		else if(e.getSource() == this.videoPrev){
			this.videoPrev.setIcon(new ImageIcon(this.getClass().getResource("avPrv.png")));
		}
		else if(e.getSource() == this.videoPlayOrStop){
			if(!this.isPlaying){
				this.videoPlayOrStop.setIcon(new ImageIcon(this.getClass().getResource("avPlay.png")));
			}else{
				this.videoPlayOrStop.setIcon(new ImageIcon(this.getClass().getResource("avPau.png")));
			}
		}
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
		if(e.getSource().equals(this.sendingButton)){
			cl.show(this.current, "Sending");
		}
		if(e.getSource().equals(this.send)){
			FileSender fileSender = new FileSender();
			
			try {
				fileSender.sendFile(new PacketsHolder(this.getTemp(),1500), clientSocket, InetAddress.getByName("localhost"));
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource().equals(this.choose)){
			int returnVal = filechooser.showOpenDialog(this);
			
			if(returnVal == JFileChooser.APPROVE_OPTION){
				this.setTemp(filechooser.getSelectedFile());
				
				System.out.println("File Name: " + this.getTemp().getName());
			}
			else {
				System.out.println("Canceled");
			}
		}
	}

	public File getTemp() {
		return temp;
	}

	public void setTemp(File temp) {
		this.temp = temp;
	}
}