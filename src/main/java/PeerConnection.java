import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PeerConnection {
    private String ip;
    private int port;

    public PeerConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void creatingConnection() {
        try {
            Socket socket = new Socket(ip, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(19);

            String response = in.readLine();
            System.out.println(response);

            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
