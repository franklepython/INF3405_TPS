import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

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

            // Read the response from client
            String[] credentials = in.readUTF().split(",");
            if (credentials.length == 2) {
                String username = credentials[0].trim();
                String password = credentials[1].trim();

                // Validate user or add a new one
                boolean isValidUser = userManager.validateUser(username);
                boolean isValidPassword = userManager.validatePassword(username, password);
                if (!isValidUser) { // If user does not exist, add them.
                    userManager.addUser(username, password);
                    out.writeUTF("New user created. You are logged in as: " + username);
                } else {
                    if (!isValidPassword) {
                        out.writeUTF("Wrong password");
                        throw new IOException("Invalid username/password.");
                    } else {
                        out.writeUTF("Hello " + username + " - you are logged in.");
                    }
                }

                while (true) {
                    String receivedMessage = in.readUTF();
                    // TODO complete the message to print with expected format

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss");
                    String date = dateFormat.format(new Date());
                    String ipAddressAndClientPort = socket.getRemoteSocketAddress().toString().substring(1);
                    // String messageToPrint = "[" + username + " - IP address:socket - DATE]: " + receivedMessage;
                    String messageToPrint = "[" + username + " - " + ipAddressAndClientPort + " - " + date + "]: " + receivedMessage;


                    // [Utilisateur 1 - 132.207.29.107:46202 - 2017-10-13@13:02:01]: Salut Utilisateur 2 !
                    // [Utilisateur 2 - 132.207.29.117:37608 - 2017-10-13@13:03:24]: Yo Utilisateur 1 !
                    
                    //System.out.println(messageToPrint);
                    Server.newMessageToProcess(messageToPrint);
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
    
    /**
	 * Send the message to the Client
	 *
	 * @param message to send
	 */
    public void sendMessageToClient(String messageToSend) {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(messageToSend);
        } catch (IOException e) {

        }
    }
}