import java.io.DataInputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            // Address and port of the server
            String serverAddress = "127.0.0.1";
            int port = 5000;

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
}

