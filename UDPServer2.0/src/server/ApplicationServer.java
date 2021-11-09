package server;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class ApplicationServer extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel panel;
	protected JPanel[] imagedPanes;
	protected JLabel image;
	
	protected EmbeddedMediaPlayerComponent player;
	String vlcPath = "C:\\Program Files\\VideoLAN\\VLC\\";
	
	protected ArrayList<File> files;
	protected Queue<ImageIcon> icons;
//	protected Queue<File> audioVideo;
	protected ArrayList<File> audioVideo;
	protected int current;
	protected String name;
	
	protected DatagramPacket receivePacket;
	protected DatagramSocket serverSocket;
	
	protected InetAddress IPAddress = null;
	protected int port = 0;
	
	CardLayout cl = new CardLayout();
	private Timer clock;
	int speed;
	
	public ApplicationServer(){
		this.setBounds(0, 0, 1366, 768);
		this.setLayout(null);
		speed = 1000;
		
		clock = new Timer(speed, new TimerListener());
		
		this.panel = new JPanel();
		this.panel.setLayout(cl);
		this.panel.setBounds(0, 0, 1366, 768);
		this.add(panel);
		
		this.imagedPanes = new JPanel[3];
		this.files = new ArrayList<File>();
		this.icons = new LinkedList<ImageIcon>();
//		this.audioVideo = new LinkedList<File>();
		this.audioVideo = new ArrayList<File>();
		this.readDirectory();
		
		for(int i = 0; i < 3; i++){
			this.imagedPanes[i] = new JPanel();
			this.imagedPanes[i].setBounds(0, 0, 1366, 768);
			this.imagedPanes[i].setVisible(true);
		}
		
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),vlcPath);
		player = new EmbeddedMediaPlayerComponent();
//		this.imagedPanes[1] = player;
		player.setVisible(true);
		
		ImageIcon tIcon = this.icons.peek();
		this.name = tIcon.getDescription();
		
		this.image = new JLabel(tIcon);
		this.imagedPanes[0].add(this.image);
		
//		this.player.getMediaPlayer().prepareMedia(this.audioVideo.peek().getAbsolutePath());
		this.current = 0;
		this.player.getMediaPlayer().prepareMedia(this.audioVideo.get(this.current).getAbsolutePath());
		
		this.panel.add("Images", this.imagedPanes[0]);
		cl.show(this.panel, "Images");
		this.panel.add("Audio/Video", this.player);
		
		this.setVisible(true);
	}
	
	private class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			try {
				refresh('n');
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void readDirectory(){
		File folder = new File("C:/Users/Jarl Brent/NETWORK SAMPLES");
		File[] listOfFiles = folder.listFiles();
		int i = 0;
		
		for (File file : listOfFiles) {
			if (file.isFile()) {
				this.setFile(file, i);
				i++;
			}
		}
	}
	
	public void setFile(File file, int i){
		this.files.add(file);
		Pattern p = Pattern.compile("[0-9A-Za-z]+$");
		Matcher m = p.matcher(this.files.get(i).getName());
		
		if(m.find()){
			if(m.group().equals("jpg") || m.group().equals("jpeg") || m.group().equals("png") || m.group().equals("ico")){
				ImageIcon temp = new ImageIcon(this.files.get(i).getPath());
				System.out.println(this.files.get(i).getPath());
				temp.setDescription(this.files.get(i).getName());
				System.out.println(temp);
				this.icons.offer(temp);
				System.out.println(this.files.get(i).getPath());
			}
			System.out.println(m.group());
			if(m.group().equals("mp4") || m.group().equals("mp3")){
				System.out.println(m.group());
//				this.audioVideo.offer(this.files.get(i));
				this.audioVideo.add(this.files.get(i));
				System.out.println(this.files.get(i).getPath());
			}
		}
	}
	
	public String getIconName(){
		return this.name;
	}
	
	public void play(String mediaPath) throws Exception{
		this.player.getMediaPlayer().playMedia(mediaPath);
		
//		String capitalizedSentence = this.audioVideo.peek().getName()
		String capitalizedSentence = this.audioVideo.get(this.current).getName();;
		UDPServer.sendData = capitalizedSentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(UDPServer.sendData, UDPServer.sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);
	}
	
	public void nextAudioVideo()throws Exception{
		this.current++;
		if(this.current == this.audioVideo.size())
			this.current = 0;
		this.play(this.audioVideo.get(this.current).getAbsolutePath());
		
//		File tAudioVid = this.audioVideo.poll();
//		System.out.println("playing n");
//		this.play(this.audioVideo.peek().getAbsolutePath());
//		System.out.println("play n");
//		this.audioVideo.offer(tAudioVid);
	}
	
	public void prevAudioVideo() throws Exception{
//		Queue<File> tAudioVideo = new LinkedList<File>();
//		File tAV;
//		
//		while(this.icons.size() > 1){
//			tAudioVideo.offer(this.audioVideo.poll());
//		}
//		tAV = this.audioVideo.poll();
//		System.out.println("playing p");
//		this.play(tAV.getAbsolutePath());
//		System.out.println("play p");
//		this.audioVideo.offer(tAV);
//		while(!tAudioVideo.isEmpty()){
//			this.audioVideo.offer(tAudioVideo.poll());
//		}
		this.current--;
		if(this.current < 0)
			this.current = this.audioVideo.size()-1;
		
		this.play(this.audioVideo.get(this.current).getAbsolutePath());
		
//		String capitalizedSentence = tAV.getName();
//		UDPServer.sendData = capitalizedSentence.getBytes();
//		DatagramPacket sendPacket = new DatagramPacket(UDPServer.sendData, UDPServer.sendData.length, IPAddress, port);
//		serverSocket.send(sendPacket);
	}
	
	public void nextImage() throws Exception{
		ImageIcon tIcon = this.icons.poll();
		System.out.println("tIcon set");
		this.image.setIcon(this.icons.peek());
		System.out.println("Image changed");
		this.name = this.icons.peek().getDescription();
		System.out.println("Name Taken");
		this.icons.offer(tIcon);
		
		String capitalizedSentence = this.getIconName();
		UDPServer.sendData = capitalizedSentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(UDPServer.sendData, UDPServer.sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);
	}
	
	public void prevImage() throws Exception{
		Queue<ImageIcon> tIcons = new LinkedList<ImageIcon>();
		ImageIcon tIcon;
		
		while(this.icons.size() > 1){
			tIcons.offer(this.icons.poll());
		}
		tIcon = this.icons.poll();
		this.image.setIcon(tIcon);
		this.name = tIcon.getDescription();
		System.out.println(tIcon.getDescription());
		this.icons.offer(tIcon);
		while(!tIcons.isEmpty()){
			this.icons.offer(tIcons.poll());
		}
		
		String capitalizedSentence = this.getIconName();
		UDPServer.sendData = capitalizedSentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(UDPServer.sendData, UDPServer.sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);
	}
	
	public void refresh(char cmd) throws Exception{
		if(cmd == UDPServer.NEXT){
			nextImage();
		}
		else if(cmd == UDPServer.PLAY_OR_STOP){
			if(clock.isRunning()){
				clock.stop();
			}
			else {
				clock.start();
				}
			}
		else if(cmd == UDPServer.PREV){
			this.prevImage();
		}
		else if(cmd == UDPServer.SPD_DWN){
			speed += 200;
			clock.setDelay(speed);
		}
		else if(cmd == UDPServer.SPD_UP){
			if(speed > 200){
				speed -= 200;
				clock.setDelay(speed);
			}
		}
		else if(cmd == UDPServer.IMAGE){
			if(this.player.getMediaPlayer().isPlaying()){
				System.out.println("pausing");
				this.player.getMediaPlayer().pause();
				System.out.println("pause");
			}
			cl.show(this.panel, "Images");
		}
		else if(cmd == UDPServer.AUDIOVIDEO){
			if(clock.isRunning()){
				clock.stop();
			}
			cl.show(this.panel, "Audio/Video");
//			this.play(this.audioVideo.peek().getAbsolutePath());
			this.play(this.audioVideo.get(this.current).getAbsolutePath());
		}
		else if (cmd == UDPServer.AVNEXT) {
			System.out.println("NEXT");
			this.nextAudioVideo();
		}
		else if (cmd == UDPServer.AVPREV) {
			System.out.println("PREV");
			this.prevAudioVideo();
		}
		else if (cmd == UDPServer.AVPLAY) {
			System.out.println("PLAY");
			if(this.player.getMediaPlayer().isPlaying()){
				System.out.println("pausing");
				this.player.getMediaPlayer().pause();
				System.out.println("pause");
			}
			else {
				System.out.println("playing");
				this.player.getMediaPlayer().play();
				System.out.println("play");
			}
		}
	}
	
	public void setReceivePacket(DatagramPacket receivePacket){
		this.receivePacket = receivePacket;
	}
	
	public DatagramPacket getReceivePacket(){
		return this.receivePacket;
	}
	
	public void setServerSocket(DatagramSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public DatagramSocket getServerSocket(){
		return this.serverSocket;
	}
	
	public void setIPAddress(InetAddress IPAddress){
		this.IPAddress = IPAddress;
	}
	
	public InetAddress getIPAddress(){
		return this.IPAddress;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	public int getPort(){
		return this.port;
	}
}