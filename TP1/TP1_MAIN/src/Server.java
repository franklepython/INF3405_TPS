import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server {
    private static ServerSocket listener;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)){
            // Address and port of the server
        	//String serverAddress = "127.0.0.1";
            //int serverPort = 5000;
        	
            String serverAddress = serverAddressValider(scanner);
			int serverPort = portValider(scanner);
			
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
                    new ClientHandler(listener.accept(), clientNumber++).start();
                }
            } finally {
                listener.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String serverAddressValider(Scanner scanner) {
		String serverAddress;
		while (true) {
			System.out.print("Saisie l'adresse IP du serveur: ");
			serverAddress = scanner.nextLine();
			
			String zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";  
			String regex = zeroTo255 + "\\."+ zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;  
			Pattern pattern = Pattern.compile(regex);  
			Matcher matcher = pattern.matcher(serverAddress);  

			if (matcher.matches()) {
				break; 
			} else {
				System.out.println("Adresse IP du serveur incorrect");
			}

		}
		return serverAddress;
	}

	private static int portValider(Scanner scanner) {
		int serverPort;
		while (true) {
			System.out.print("Saisie le port du serveur entre 5000 et 5050: ");
			try {
				serverPort = Integer.parseInt(scanner.nextLine());
				if (serverPort >= 5000 && serverPort <= 5050) {
					break; 
				} else {
					System.out.println("Port invalide, il doit etre entre 5000 et 5050");
				}
			} catch (NumberFormatException e) {
				System.out.println("Port invalide, cela doit etre un nombre");
			}
		}
		return serverPort;
	}
}