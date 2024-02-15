import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import java.util.Scanner;
// Utilisation de scanner pour la saisie au clavier

import java.util.regex.Matcher;
import java.util.regex.Pattern;
// Pour verifier si c'est un adresse IP de 4 octet

public class Client {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Get server address and port
            String serverAddress = serverAddressValider(scanner);
            int port = portValider(scanner);

            // Get username and password from user
            System.out.print("Saisie un nom d'utilisateur: ");
            String username = scanner.nextLine();
            System.out.print("Saisie un mot de passe: ");
            String password = scanner.nextLine();

            try (Socket socket = new Socket(serverAddress, port);
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                 DataInputStream in = new DataInputStream(socket.getInputStream())) {
                
                System.out.format("Connected to server at [%s:%d]%n", serverAddress, port);

                // Send username and password to server
                out.writeUTF(username + "," + password);

                // Wait for the server's response
                String responseFromServer = in.readUTF();
                System.out.println(responseFromServer);
                
                // Optionally: Handle further interactions based on server's response
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
	/** 
     * Vérifie si l'addresse IP est sur 4 octet
     * @param scanner pour pouvoir lire les entrées de l'utilisateur
     * @return l'addresse du server vérifié 
     */
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
	
	/** 
     * Vérifie si le port est un nombre et si il est entre 5000 et 5050
     * @param scanner pour pouvoir lire les entrées de l'utilisateur
     * @return le port validé
     */
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