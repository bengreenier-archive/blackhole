import java.util.Scanner;

public class Blackhole {
	
	public static void main(String args[]) {
		Blackhole blackhole = new Blackhole();
		blackhole.Start();
		
	}
	public void Start() {
		System.out.print("Server (s) or Client (c): ");
		Scanner keyboard = new Scanner(System.in);
		
		String blackholeSystem = keyboard.next();
		switch(Character.toLowerCase(blackholeSystem.charAt(0))) {
		case 's':
			System.out.println("Staring Blackhole Server..");
			StartServer();
			break;
		case 'c':
			System.out.println("Staring Blackhole Client...");
			StartClient();
			break;
		default:
			System.out.println("Not a valid input...");
			System.out.println("Quitting application");
			System.exit(1);
			break;
		}
	}
	public void StartServer() {
		BlackholeServer blackholeServer = new BlackholeServer();
		try {
			blackholeServer.ExitHole();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void StartClient() {
		BlackholeClient blackholeClient = new BlackholeClient();
		Scanner keyboard = new Scanner(System.in);
		boolean running = true;
		try {
			while(running) {
				System.out.print("Server IP Address: ");
				String ipAddress = keyboard.next();
				if(ipAddress.equals("q")) {
					running = false;
				} else {
					if(blackholeClient.Connect(ipAddress)) {
						System.out.println("Successfully Connected...");
						while(true) {
							blackholeClient.Client(ipAddress);
						}
					} else {
						System.out.println("Could not connect...");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
