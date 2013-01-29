import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BlackholeServer {
	private int port = 53535;
	
	public void ExitHole() throws Exception {
		DatagramSocket serverSocket = new DatagramSocket(port);
		byte[] receiveData;
		byte[] sendData;
		DatagramPacket receivePacket;
		//String sentence;
		InetAddress IPAddress;
		//String capitalizedSentence;
		DatagramPacket sendPacket;
		//int port;
		File newFile = null;
		FileOutputStream fos = null;
		String newFilename = "";
		int filebytesize=1024;
		int packetsBeingSent = 1;
		int packetSize = 49152;
		
		while(true) {
			receiveData = new byte[1024];
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			
			//System.out.println(""+receivePacket.getData()[0]);
			
			if(receivePacket.getData()[0]==0) {
				// this packet contains the filename and extension
				receiveData = new byte[1024];
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				newFilename = new String(receivePacket.getData());
				System.out.println("Creating new file "+newFilename);
				newFile = new File(newFilename);
				int fileNumber = 1;
				while(newFile.exists()) {
					String newNewFilename = newFilename.substring(0, newFilename.indexOf("."))+"("+fileNumber+")"+newFilename.substring(newFilename.indexOf("."));
					newFile = new File(newNewFilename);
					fileNumber++;
				}
				fos = new FileOutputStream(newFile);
				
				// receiving file size
				receiveData = new byte[1024];
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				receiveData = RemoveBlanks(receivePacket.getData());
				String filesize = new String(receiveData);
				//System.out.println(filesize);
				filebytesize = Integer.parseInt(filesize);
				
				// receiving number of packets being sent
				receiveData = new byte[1024];
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				receiveData = RemoveBlanks(receivePacket.getData());
				String packetLength = new String(receiveData);
				//System.out.println(packetLength);
				packetsBeingSent = Integer.parseInt(packetLength);
			} else if(receivePacket.getData()[0]==1) {
				// this packet contains the actual file data
				System.out.println("Writing data to file "+newFilename);
				int packetsReceived = 0;
				int datasizesofar = 0;
				double current = 0;
				long startTime = System.currentTimeMillis();
				while (packetsReceived < packetsBeingSent) {
					receiveData = new byte[packetSize];
					receivePacket = new DatagramPacket(receiveData, receiveData.length);
					serverSocket.receive(receivePacket);
					fos.write(receivePacket.getData(), 0, packetSize);
					datasizesofar+=receivePacket.getData().length;
					current++;
					if(current>=100) {
						System.out.println((int)((double)datasizesofar/((packetsBeingSent+1)*packetSize)*100)+"%");
						current = 0;
					}
					//System.out.println(datasizesofar);
					packetsReceived++;
					//System.out.println("Received packet "+packetsReceived+" out of "+packetsBeingSent);
					
					IPAddress = receivePacket.getAddress();
					sendData = new byte[1];
					sendData[0]=1;
					sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
					serverSocket.send(sendPacket);
				}
				receiveData = new byte[filebytesize];
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				fos.write(receivePacket.getData(), 0, filebytesize);
				System.out.println("Finished writing data to file "+newFilename);
				long endTime = System.currentTimeMillis();
				System.out.println("Total transfer time: "+(endTime-startTime)/1000);
				
				IPAddress = receivePacket.getAddress();
				sendData = new byte[1];
				sendData[0]=1;
				sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
				serverSocket.send(sendPacket);
				
				fos.close();
			} else if(receivePacket.getData()[0]==2) {
				// this packet is a connecting packet, respond to 2
				IPAddress = receivePacket.getAddress();
				System.out.println("Connecting with "+IPAddress.getHostAddress());
				sendData = new byte[1];
				sendData[0]=2;
				sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, receivePacket.getPort());
				serverSocket.send(sendPacket);
			}
			//sentence = new String(receivePacket.getData());
			//System.out.println("RECEIVED: "+sentence);
			
			//IPAddress = receivePacket.getAddress();
			//port = receivePacket.getPort();
			
			//capitalizedSentence = sentence.toUpperCase();
			
			//sendData = new byte[capitalizedSentence.getBytes().length];
			//sendData = capitalizedSentence.getBytes();
			//sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			//serverSocket.send(sendPacket);
		}
	}
	private byte[] RemoveBlanks(byte[] byteArray) {
		int end = 0;
		for(int i = 0; i < byteArray.length; i++) {
			if(byteArray[i]==0) {
				end=i;
				i=byteArray.length;
			}
		}
		byte[] returnByteArray = new byte[end];
		for(int i = 0; i < returnByteArray.length; i++) {
			returnByteArray[i]=byteArray[i];
		}
		return returnByteArray;
	}
}
