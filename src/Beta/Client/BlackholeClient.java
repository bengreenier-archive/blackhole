

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
import java.util.Scanner;

public class BlackholeClient {
	final int PORT = 53535;
	
	public static void main(String args[]) {
		System.out.println("Starting Blackhole Client...");
		BlackholeClient blackholeClient = new BlackholeClient();
		if(args.length>0) {
			blackholeClient.Start(args[0]);
		} else {
			System.out.println("No input file, quiting application...");
		}
	}
	
	DatagramSocket clientSocket;
	InetAddress IPAddress;
	byte[] sendData;
	byte[] receiveData;
	byte[] deck;
	DatagramPacket sendPacket;
	DatagramPacket receivePacket;
	int packetSize = 49152;
	boolean connected = false;
	
	public void Start(String input_file) {
		Scanner keyboard = new Scanner(System.in);
		for(int i = input_file.length()-1; i > 0; i--) {
			if(input_file.charAt(i)=='\\') {
				input_file = input_file.substring(i+1);
				i = 0;
			}
		}
		System.out.println(input_file);
		boolean running = true;
		try {
			while(running) {
				System.out.print("Server IP Address: ");
				String ipAddress = keyboard.next();
				if(ipAddress.equals("q")) {
					System.out.println("Quitting...");
					running = false;
				} else {
					if(Connect(ipAddress)) {
						System.out.println("Successfully Connected...");
						Client(ipAddress, input_file);
						running = false;
					} else {
						System.out.println("Could not connect...");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void Client(String ipAddress, String filename) throws Exception {
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName(ipAddress);
		//BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		//System.out.print("Filename to send: ");
		//String filename = inFromUser.readLine();
		File sendfile = new File(filename);
		FileInputStream fis = new FileInputStream(sendfile);
		
		System.out.print("Sending filename and extension...");
		
		sendData = new byte[1];
		sendData[0]=0;
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
		clientSocket.send(sendPacket);
		
		sendData = new byte[filename.getBytes().length];
		sendData=filename.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
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
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
		clientSocket.send(sendPacket);
		
		// Send number of packets to expect
		sendData = new byte[(Integer.toString(numberOfPackets).getBytes().length)];
		sendData=(Integer.toString(numberOfPackets)).getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
		clientSocket.send(sendPacket);
		
		System.out.println("Sent.");
		System.out.print("Sending file data...");
		sendData = new byte[1];
		sendData[0]=1;
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
		clientSocket.send(sendPacket);
		clientSocket.setSoTimeout(750);
		// Send the data
		while(bytesSent < numberOfPackets) {
			sendData = new byte[packetSize];
			fis.read(sendData, 0, packetSize);
			boolean runagain = false;
			do {
				runagain = false;
				sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
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
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
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
	public boolean Connect(String ipAddress) throws Exception {
		boolean safe = false;
		
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName(ipAddress);
		byte[] sendData = new byte[1];
		byte[] receiveData = new byte[1];
		sendData[0]=2;
		// Send connected signal
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
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
	public boolean SendDeck() {
		return Send(deck);
    }
    public boolean Send(byte[] data) {
    	boolean worked = false;
    	sendData = data;
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
		try {
			clientSocket.send(sendPacket);
			worked = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return worked;
    }
    public static byte[] getBytesFromFile(File file) throws IOException {        
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            throw new IOException("File is too large!");
        }

        byte[] bytes = new byte[(int)length];

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

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
        return bytes;
    }
}
