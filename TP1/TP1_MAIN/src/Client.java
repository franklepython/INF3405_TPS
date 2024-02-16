import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

				if (responseFromServer.equals("Wrong password")) {
					// Optionally: Handle further interactions based on server's response

				} else {
					new ClientServerListener(in).start();

					// client must continuously listen to messages received by server (socket's
					// DataInputStream) - for messages sent by other users, to be displayed with
					// System.out.println
					// client must continuously expect input from user (scanner.nextLine), and send
					// it through socket's DataOutputStream
					while (true) {
						// read user input, send to server
						String userMessageString = scanner.nextLine();
						if (userMessageString.length() <= 200) {
							out.writeUTF(userMessageString);
						} else {
							// TODO
							System.out.println("Message too long. Please enter a message of up to 200 characters.");
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

class ClientServerListener extends Thread {
	private DataInputStream in;

	public ClientServerListener(DataInputStream in) {
		this.in = in;
	}

	public void run() {
		try {
			while (true) {
				// read server input, display on screen
				String serverInput = this.in.readUTF();
				System.out.println(serverInput);
			}
		} catch (IOException e) {

		}
	}
}