import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private int clientNumber;
    private UserManager userManager;

    public ClientHandler(Socket socket, int clientNumber, UserManager userManager) {
        this.socket = socket;
        this.clientNumber = clientNumber;
        this.userManager = userManager;
        System.out.println("New connection with client#" + clientNumber + " at " + socket);
    }

    public void run() {
        try (DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            // Ask for username and password
            out.writeUTF("Please enter your username and password separated by a comma:");

            // Read the response from client
            String[] credentials = in.readUTF().split(",");
            if (credentials.length == 2) {
                String username = credentials[0].trim();
                String password = credentials[1].trim();

                // Validate user or add a new one
                boolean isValidUser = userManager.validateUser(username, password);
                if (!isValidUser) { // If user does not exist, add them.
                    userManager.addUser(username, password);
                    out.writeUTF("New user created. You are logged in as: " + username);
                } else {
                    out.writeUTF("Hello " + username + " - you are logged in.");
                }
            } else {
                out.writeUTF("Invalid login format. Connection will be closed.");
            }
        } catch (IOException e) {
            System.out.println("Error handling client# " + clientNumber + ": " + e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Couldn't close a socket, what's going on?");
            }
            System.out.println("Connection with client# " + clientNumber + " closed");
        }
    }
}