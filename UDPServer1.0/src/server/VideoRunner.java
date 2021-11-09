package server;

import javax.swing.JFrame;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class VideoRunner {
	private JFrame frame = new JFrame();
	
	private EmbeddedMediaPlayerComponent player;
	
	private String path = "";
	
	public VideoRunner(String vlcPath, String mediaPath){
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),vlcPath);
		
		path = mediaPath;
		
		player = new EmbeddedMediaPlayerComponent();
		
		frame.setContentPane(player);
		
		frame.setSize(1280, 720);
		
		frame.setVisible(true);
	}
	
	public void run(){
		this.player.getMediaPlayer().playMedia(this.path);
	}
}
