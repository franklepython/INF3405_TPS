import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Server {
    private static ServerSocket listener;
    private static UserManager userManager;
    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();

    //127.0.0.1:5000
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)){
            // Address and port of the server
            String serverAddress = serverAddressValider(scanner);
            int serverPort = portValider(scanner);

            // Create the UserManager instance
            userManager = new UserManager("./users.csv");
            
            // Create the connection for communicating with clients
            listener = new ServerSocket();
            listener.setReuseAddress(true);
            InetAddress serverIP = InetAddress.getByName(serverAddress);

            // Associate the address and port with the connection
            listener.bind(new InetSocketAddress(serverIP, serverPort));
            System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);

            int clientNumber = 0;
            try {
                while (true) {
                    // Wait for a new client to connect
                    Socket clientSocket = listener.accept(); // Accept the client connection
                    // Pass the UserManager instance to the new ClientHandler
                    
                    ClientHandler clientHandler = new ClientHandler(clientSocket, clientNumber++, userManager);
                    clientHandler.start();
                    clientHandlers.add(clientHandler);
                }
            } finally {
                listener.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void newMessageToProcess(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessageToClient(messageToSend);
        }

        // TODO - save message in database
    }
    
    /**
	 * Checks if the IP address has 4 octets.
	 *
	 * @param scanner to read user inputs
	 * @return the verified server address
	 */
	private static String serverAddressValider(Scanner scanner) {
		String serverAddress;
		while (true) {
			System.out.print("Enter the server's IP address: ");
			serverAddress = scanner.nextLine();

			String zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";
			String regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(serverAddress);

			if (matcher.matches()) {
				break;
			} else {
				System.out.println("Incorrect server IP address");
			}

		}
		return serverAddress;
	}

	/**
	 * Checks if the port is a number and if it is between 5000 and 5050.
	 *
	 * @param scanner to read user inputs
	 * @return the validated port
	 */
	private static int portValider(Scanner scanner) {
		int port;
		while (true) {
			System.out.print("Enter the server port between 5000 and 5050: ");
			try {
				port = Integer.parseInt(scanner.nextLine());
				if (port >= 5000 && port <= 5050) {
					break;
				} else {
					System.out.println("Invalid port, it must be between 5000 and 5050");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid port, it must be a number.");
			}
		}
		return port;
	}
}