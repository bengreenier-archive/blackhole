import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;


public class BlackholeClient {
	private int port = 53535;
	
	DatagramSocket clientSocket;
	InetAddress IPAddress;
	byte[] sendData;
	byte[] receiveData;
	byte[] deck;
	DatagramPacket sendPacket;
	DatagramPacket receivePacket;
	int packetSize = 49152;
	boolean connected = false;
	
	public boolean Connect(String ipAddress) throws Exception {
		boolean safe = false;
		
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName(ipAddress);
		byte[] sendData = new byte[1];
		byte[] receiveData = new byte[1];
		sendData[0]=2;
		// Send connected signal
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
		// Wait for connected signal
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		if(receivePacket.getData()[0]==2) {
			safe = true;
		}
		connected = safe;
		return safe;
	}
	
	public void Client(String ipAddress) throws Exception {
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName(ipAddress);
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Filename to send: ");
		String filename = inFromUser.readLine();
		File sendfile = new File(filename);
		FileInputStream fis = new FileInputStream(sendfile);
		
		System.out.print("Sending filename and extension...");
		
		sendData = new byte[1];
		sendData[0]=0;
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
		
		sendData = new byte[filename.getBytes().length];
		sendData=filename.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
		
		// send file size
		System.out.println("Sent.");
		System.out.print("Sending file size ");
		int fileSize = (getBytesFromFile(sendfile).length);
		int numberOfPackets = (fileSize/packetSize);
		System.out.print(" ("+(Integer.toString(fileSize%packetSize))+"("+numberOfPackets+"))... ");
		int bytesSent = 0;
		sendData = new byte[(Integer.toString(fileSize%packetSize).getBytes().length)];
		sendData=(Integer.toString(fileSize%packetSize)).getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
		
		// Send number of packets to expect
		sendData = new byte[(Integer.toString(numberOfPackets).getBytes().length)];
		sendData=(Integer.toString(numberOfPackets)).getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
		
		System.out.println("Sent.");
		System.out.print("Sending file data...");
		sendData = new byte[1];
		sendData[0]=1;
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
		clientSocket.setSoTimeout(750);
		// Send the data
		while(bytesSent < numberOfPackets) {
			sendData = new byte[packetSize];
			fis.read(sendData, 0, packetSize);
			boolean runagain = false;
			do {
				runagain = false;
				sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				clientSocket.send(sendPacket);
				
				//System.out.println("Sending packet "+bytesSent+" out of "+numberOfPackets);
				receiveData = new byte[1];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				try {
				clientSocket.receive(receivePacket);
				} catch (SocketTimeoutException e) {
					System.out.println("Timed out, trying to resend "+bytesSent+" again...");
					runagain=true;
				}
				receiveData=receivePacket.getData();
			} while (runagain);
			
			bytesSent++;
			
			if(receiveData[0]!=1) {
				bytesSent = numberOfPackets;
			}
		}
		
		sendData = new byte[fileSize%packetSize];
		fis.read(sendData, 0, fileSize%packetSize);
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
		
		receiveData = new byte[1];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		receiveData=receivePacket.getData();
		if(receiveData[0]==1) {
			System.out.println("Sent sucessfully.");
		} else {
			System.out.println("Error during sending.");
		}
		fis.close();
		clientSocket.close();
	}
	public void RunClient() throws Exception {
		while(connected) {
			try {
				// select file name
				BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
				System.out.print("Filename to send: ");
				String filename = inFromUser.readLine();
				if(filename.equals("q")) {
					connected = false;
					return;
				}
				File sendfile = new File(filename);
				FileInputStream fis = new FileInputStream(sendfile);
				
				// send start signal
				deck = new byte[1];
				deck[0]=0;
				SendDeck();
				
				// send filename
				System.out.print("Sending filename and extension...");
				deck = filename.getBytes();
				if(SendDeck()) {
					System.out.println("Sent.");
				}
				// send file size
				System.out.print("Sending file size ");
				int fileSize = (getBytesFromFile(sendfile).length);
				deck=(Integer.toString(fileSize)).getBytes();
				if(SendDeck()) {
					System.out.println("Sent.");
				}
				
				// send data
				SendFile(fis);
			} catch (Exception e) {
				System.out.println(e.toString());
				System.out.println("Disconnecting..");
				connected = false;
			}
		}
	}
	public void SendFile(FileInputStream fis) {
		
	}
	// Returns the contents of the file in a byte array.
    public static byte[] getBytesFromFile(File file) throws IOException {        
        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
            throw new IOException("File is too large!");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;

        InputStream is = new FileInputStream(file);
        try {
            while (offset < bytes.length
                   && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }
        } finally {
            is.close();
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
        return bytes;
    }
    
    public boolean SendDeck() {
    	boolean worked = false;
    	
    	sendData = deck;
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		try {
			clientSocket.send(sendPacket);
			worked = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return worked;
    }
    public boolean Send(byte[] data, int code) {
    	boolean worked = false;
    	sendData = data;
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		try {
			clientSocket.send(sendPacket);
			worked = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return worked;
    }
}
