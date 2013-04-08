import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;


public class UDPClient {
	
	private DatagramSocket clientSocket;
	private byte[] sendingData = new byte[1000];
	private byte[] receivedData = new byte[1000];
	private ArrayList<T_Packet> received = new ArrayList<T_Packet>();
	private static int experimentTime = 1; /*experiment duration in seconds*/ 
	
	
	private void SendMsg(InetAddress serverAddress, int port, String msg) throws IOException{
		Date start;
		Date end = new Date();
		sendingData = msg.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendingData, sendingData.length, serverAddress, port);
		clientSocket = new DatagramSocket();
		clientSocket.send(sendPacket);
		System.out.println("Client sent a request to " + serverAddress.toString() + " port " + port + " msg " + msg);

		/*client starts listening*/
		DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
		start = new Date();	/*get current time*/
		String receivedString = "-1";
		while ( (end.getTime()-start.getTime())/1000 < experimentTime ){
			clientSocket.receive(receivedPacket);
			//Something received from the server
			receivedString = new String (receivedPacket.getData());
			System.out.println("Client: " + receivedString);
			received.add(new T_Packet(receivedString));
			end = new Date();
		}
		System.out.println("Experiment ended");
	}
	
	private void CalculateResult(){
		System.out.println("Received:");
		int cs = 0;
		int r = 0;
		ArrayList<Integer> lostCount = new ArrayList<Integer>();
		for (int i = 0; i < received.size(); ++i){
			r = Integer.parseInt(StringCut(received.get(i).seq));
			System.out.println("r/cs = "+ r+" "+cs);
			if (cs != r ){ //loss occurs
				lostCount.add(r-cs);	/*number of lost pkts added into lostCount list*/
				cs = r;
			}
			cs++;
		}
		
		System.out.println("Lost:");
		for (int i = 0; i < lostCount.size(); ++i){
			System.out.println(lostCount.get(i));
		}
	}
	
	private String StringCut(String str){	/*return only integer inside a 1000-byte string*/
		int i = 0;
		for (i = 0; i < str.length(); ++i){
			if (str.charAt(i) < '0'){
				break;
			}
		}
		return str.substring(0, i);
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		UDPClient client = new UDPClient();
		client.SendMsg(InetAddress.getByName("155.98.65.57"), 6789, new String(Integer.toString(experimentTime)));
		client.CalculateResult();
	}

}
