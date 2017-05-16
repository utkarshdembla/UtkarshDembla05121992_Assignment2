import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;



public class ServerSetup {
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;

	private static final int maxClientsCount = 10;
	private static final clientImplementation[] threads = new clientImplementation[maxClientsCount];

	public static void main(String args[]) {

	
		int portNumber = 2222;
		if (args.length < 1) {
			System.out.println(
					"Using port number : " + portNumber);
		} else {
			portNumber = Integer.valueOf(args[0]).intValue();
		}

		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			System.out.println(e);
		}

		
		
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				int i = 0;
				for (i = 0; i < maxClientsCount; i++) {
					if (threads[i] == null) {
						(threads[i] = new clientImplementation(clientSocket, threads)).start();
						break;
					}
				}
				if (i == maxClientsCount) {
					PrintStream os = new PrintStream(clientSocket.getOutputStream());
					os.println("Server too busy.Please try later.");
					os.close();
					clientSocket.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}
