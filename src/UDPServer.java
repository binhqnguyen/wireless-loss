import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UDPServer {
	private byte[] receivedData = new byte[1000];
	private byte[] sendingData = new byte[1000];
	private int sendingPort = 0;
	private InetAddress clientAddress;
	private DatagramSocket serverSocket;
	private int seq = 0;	/*sequence number of sending UDP packet*/ 
	
	public void Listen(int port) throws IOException, InterruptedException{
		serverSocket = new DatagramSocket(port);
		System.out.println("Server is listenning on port " + port);
		while (true){
			DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
			serverSocket.receive(receivedPacket);
			/*something received from the client*/
			System.out.println("Server received " + new String(receivedPacket.getData()));
			String data = new String(receivedPacket.getData());
			clientAddress = receivedPacket.getAddress();
			sendingPort = receivedPacket.getPort();
			
			if (data.equals(StringCut("request"))){	//reply the client's request.
				StartReply();
			}
		}
	}

			
	private void StartReply() throws IOException, InterruptedException{
		seq = 0;	/*reset the seq number*/
		DatagramPacket sendPacket;
		System.out.println("Server stats replying ....");
		while (true){
			sendingData = Integer.toString(seq).getBytes();
			sendPacket = new DatagramPacket(sendingData, sendingData.length, clientAddress, sendingPort);
			serverSocket.send(sendPacket);
			System.out.println("Server: " +seq);
			seq++;
			Thread.sleep(1);
		}
	}
	
	private String StringCut(String str){	/*return only integer inside a 1000-byte string*/
		int i = 0;
		for (i = 0; i < str.length(); ++i){
			if (str.charAt(i) < '0' || str.charAt(i) > '9'){
				break;
			}
		}
		return str.substring(0, i);
	}
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		UDPServer server = new UDPServer();
		server.Listen(6789);
	}
	
}
