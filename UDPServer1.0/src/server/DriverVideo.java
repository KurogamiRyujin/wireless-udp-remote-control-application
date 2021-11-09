package server;

import java.io.File;

import javax.swing.JFileChooser;

public class DriverVideo {

	private static JFileChooser fSelector = new JFileChooser();
	
	public static void main(String[] args){
		String vlcPath = "C:/Program Files/VideoLAN/VLC/";
//		String mediaPath = "C:\\Users\\Jarl Brent\\Desktop\\Anime\\Steins;Gate - 23_HD.mp4";
		String mediaPath = "C:\\Users\\Jarl Brent\\Music\\aLIEz - Aldnoah.Zero Ending Theme.mp3";
		
//		String vlcPath = "C:/Program Files/VideoLAN/VLC";
//		//String mediaPath = "C:\\Users\\Jazmine\\Pictures\\What is Innovation.mkv";
//		String mediaPath = "C:\\Users\\Jazmine\\Downloads\\The.Big.Bang.Theory.S09E16.720p.HDTV.2CH.x265.HEVC-PSA.mkv";
		//String vlcPath = "";
		//String mediaPath = "";
//		File f;
		
//		fSelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		fSelector.showSaveDialog(null);
//		f = fSelector.getSelectedFile();
//		vlcPath = f.getAbsolutePath();
//		
//		fSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
//		fSelector.showSaveDialog(null);
//		f = fSelector.getSelectedFile();
//		mediaPath = f.getAbsolutePath();
//		System.out.println(f.getAbsolutePath());
		
		
		new VideoRunner(vlcPath,mediaPath).run();
		
		
	}
}
