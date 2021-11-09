package client;

class UDPClient {
	public static final char PLAY_OR_STOP = 'p', SPD_UP = 'f', SPD_DWN = 's', NEXT = 'n', PREV = 'b', EXIT = 'x',VIDEO = 'v', IMAGE = 'i', BACK = 'm';
	
	public static void main(String args[]) throws Exception
	{
		new ApplicationClient();
	}
}