package server;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ApplicationServer extends JFrame /*implements KeyListener*/{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel panel;
	protected JPanel[] imagedPanes;
	protected JLabel image;
	
	protected ArrayList<File> files;
	protected Queue<ImageIcon> icons;
	protected String name;
	
	CardLayout cl = new CardLayout();
	Timer clock;
	int speed;
	
	public ApplicationServer(){
		this.setBounds(0, 0, 1366, 768);
		this.setLayout(null);
//		this.addKeyListener(this);
		speed = 1000;
		
		clock = new Timer(speed, new TimerListener());
//		clock = null;
		
		this.panel = new JPanel();
		this.panel.setLayout(cl);
		this.panel.setBounds(0, 0, 1366, 768);
		this.add(panel);
		
		this.imagedPanes = new JPanel[3];
		this.files = new ArrayList<File>();
//		this.files = new File[3];
//		this.files[0] = new File("C:/Users/Jarl Brent/Pictures/Reina.png");
//		this.files[1] = new File("C:/Users/Jarl Brent/Pictures/Strike the Blood Theme/astarte1.png");
//		this.files[2] = new File("C:/Users/Jarl Brent/Pictures/Strike the Blood Theme/natsuki2.png");
		this.icons = new LinkedList<ImageIcon>();
		this.readDirectory();
		
		for(int i = 0; i < 3; i++){
			this.imagedPanes[i] = new JPanel();
			this.imagedPanes[i].setBounds(0, 0, 1366, 768);
			this.imagedPanes[i].setVisible(true);
			
//			this.setFile(files, i);
		}
		
//		Icon tempIcon = this.icons.peek();
		ImageIcon tIcon = this.icons.peek();
//		this.icons.offer(tIcon);
		this.name = tIcon.getDescription();
		
		
		this.image = new JLabel(tIcon);
//		this.nextImage();
		this.imagedPanes[0].add(this.image);
		
		this.panel.add("Images", this.imagedPanes[0]);
		cl.show(this.panel, "Images");
		this.panel.add("Audio", this.imagedPanes[1]);
		
		this.setVisible(true);
	}
	
	private class TimerListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e){
			nextImage();
		}
	}
	
	public void readDirectory(){
		File folder = new File("C:/Users/Jarl Brent/Pictures/Camera Roll/");
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
//				this.icons[i] = new ImageIcon(file[i].getPath());
				ImageIcon temp = new ImageIcon(this.files.get(i).getPath());
				System.out.println(this.files.get(i).getPath());
				temp.setDescription(this.files.get(i).getName());
				System.out.println(temp);
				this.icons.offer(temp);
				System.out.println(this.files.get(i).getPath());
			}
		}
	}
	
	public String getIconName(){
		return this.name;
	}
	
	public void nextImage() {
		ImageIcon tIcon = this.icons.poll();
		this.image.setIcon(this.icons.peek());
		this.name = this.icons.peek().getDescription();
		this.icons.offer(tIcon);
		
//		System.out.println("RECEIVED: " + sentence);
//		
//		InetAddress IPAddress = receivePacket.getAddress();
//		
//		int port = receivePacket.getPort();
//		String capitalizedSentence = this.getIconName();
//		UDPServer.sendData = capitalizedSentence.getBytes();
//		DatagramPacket sendPacket = new DatagramPacket(UDPServer.sendData, UDPServer.sendData.length, IPAddress, port);
//		serverSocket.send(sendPacket);
	}
	
	public void prevImage(){
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
	}
	
	public void refresh(char cmd) {
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
	}

//	@Override
//	public void keyPressed(KeyEvent e) {
//		// TODO Auto-generated method stub
//		if(e.getKeyChar() == UDPServer.NEXT){
//			nextImage();
//		}
//		else if(e.getKeyChar() == UDPServer.PLAY_OR_STOP){
//			if(clock.isRunning()){
//				clock.stop();
//			}
//			else {
//				clock.start();
//			}
//		}
//		else if(e.getKeyChar() == UDPServer.SPD_DWN){
//			speed += 200;
//			clock.setDelay(speed);
//		}
//		else if(e.getKeyChar() == UDPServer.SPD_UP){
//			if(speed > 200){
//				speed -= 200;
//				clock.setDelay(speed);
//			}
//		}
//		else if(e.getKeyChar() == UDPServer.AUDIO){
//			cl.show("", arg1);
//		}
//	}

//	@Override
//	public void keyReleased(KeyEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void keyTyped(KeyEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
}
