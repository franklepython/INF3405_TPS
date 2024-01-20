import java.io.DataInputStream;
import java.net.Socket;

import java.util.Scanner;
// Utilisation de scanner pour la saisie au clavier

import java.util.regex.Matcher;
import java.util.regex.Pattern;
// Pour verifier si c'est un adresse IP de 4 octet

public class Client {
	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)){ 
			// Address and port of the server
			// String serverAddress = "127.0.0.1";
			// int port = 5000;

			String serverAddress = serverAddressValider(scanner);
			int port = portValider(scanner);
			
			System.out.print("Saisie un nom d'utilisateur: ");
			String nom = scanner.nextLine();
			System.out.print("Saisie un mot de passe: ");
			String mdp = scanner.nextLine();
			// Potentiellement envoie en socket dans un try pour l'envoyer au server
			
			// Create a new connection to the server
			try (Socket socket = new Socket(serverAddress, port)) {
				System.out.format("Connected to server at [%s:%d]%n", serverAddress, port);

				// Create an incoming channel to receive messages sent by the server
				DataInputStream in = new DataInputStream(socket.getInputStream());

				// Wait for the reception of a message sent by the server on the channel
				String helloMessageFromServer = in.readUTF();
				System.out.println(helloMessageFromServer);
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
		int port;
		while (true) {
			System.out.print("Saisie le port du serveur entre 5000 et 5050: ");
			try {
				port = Integer.parseInt(scanner.nextLine());
				if (port >= 5000 && port <= 5050) {
					break; 
				} else {
					System.out.println("Port invalide, il doit etre entre 5000 et 5050");
				}
			} catch (NumberFormatException e) {
				System.out.println("Port invalide, cela doit etre un nombre");
			}
		}
		return port;
	}
}

