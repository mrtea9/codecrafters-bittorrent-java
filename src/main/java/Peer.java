import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.Type;
import com.google.gson.Gson;

public class Peer {
    private final Torrent torrent;
    private static final Gson gson = new Gson();
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
            URL url = new URL(full_request);
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            // System.out.println("responseCode = " + responseCode);
            byte[] bytes = connection.getInputStream().readAllBytes();
            Map<String, Object> result =
                    bencode.decode(bytes, Type.DICTIONARY);
            ByteBuffer peersBuffer = (ByteBuffer)result.get("peers");
            System.out.println(gson.toJson(peersBuffer));
            System.out.println(gson.toJson(result));
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
