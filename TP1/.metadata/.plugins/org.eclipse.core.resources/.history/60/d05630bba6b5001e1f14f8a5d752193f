import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Server {
    private static ServerSocket listener;

    public static void main(String[] args) {
        try {
            // Address and port of the server
            String serverAddress = "127.0.0.1";
            int serverPort = 5000;

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
}