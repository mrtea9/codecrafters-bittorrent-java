import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class PeerConnection {
    private String ip;
    private int port;
    private Torrent torrent;

    public PeerConnection(String ip, int port, Torrent torrent) {
        this.ip = ip;
        this.port = port;
        this.torrent = torrent;
    }

    public void creatingConnection() {

        ByteBuffer handshake = constructHandshake(this.torrent);

        try {
            Socket socket = new Socket(ip, port);
            OutputStream out = socket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.write(handshake.array());

            String response = in.readLine();
            System.out.println(response);

            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private ByteBuffer constructHandshake(Torrent torrent) {
        String peerId = "23141516167152146123";
        ByteBuffer handshake = ByteBuffer.allocate(49 + 19);

        handshake.put((byte) 19);
        handshake.put("BitTorrent protocol".getBytes());
        handshake.put(new byte[8]);
        handshake.put(this.torrent.infoHashBytes);
        handshake.put(peerId.getBytes());

        return handshake;
    }
}
