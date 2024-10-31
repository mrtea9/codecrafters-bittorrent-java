import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.dampcake.bencode.Bencode;

public class Peer {
    private final Torrent torrent;
    private final Bencode bencode = new Bencode();

    public Peer(Torrent torrent) {
        this.torrent = torrent;
    }

    public String discoverPeers() {
        String full_address = this.torrent.announce;

        performGetRequest();

        return full_address;
    }

    public void performGetRequest() {

        HttpClient client = HttpClient.newHttpClient();

        String peerId = "23141516167152146123";
        String full_request = (
                this.torrent.announce + "?info_hash="
                + URLEncoder.encode(
                        new String(this.torrent.infoHashBytes, StandardCharsets.ISO_8859_1),
                        StandardCharsets.ISO_8859_1
                )
                + "&peer_id=" + peerId + "&port=6881&uploaded=0&downloaded=0&left=" + this.torrent.length
                + "&compact=1"
        );

        HttpRequest request = HttpRequest.newBuilder(
                URI.create(full_request))
                .header("accept", "*/*")
                .build();

        try {
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            System.out.println("status code: " + response.statusCode());
            byte[] responseBodyBytes = response.body();
            StringBuilder hexString = new StringBuilder(2 * responseBodyBytes.length);
            for (byte b : responseBodyBytes) {
                hexString.append(String.format("%02x", b));
            }
            System.out.println("response body in hex: " + hexString.toString());
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
