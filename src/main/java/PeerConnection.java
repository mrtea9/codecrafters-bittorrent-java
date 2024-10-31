import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

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
            byte[] response = new byte[68];

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            out.write(handshake.array());
            int bytesRead = in.read(response);

            System.out.println("bytesRead = " + bytesRead);
            byte[] receivedPeerId = new byte[20];
            System.arraycopy(response, 48, receivedPeerId, 0, 20);
            System.out.println("Peer ID: " + Torrent.bytesToHex(receivedPeerId));

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
