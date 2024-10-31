import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

public class Peer {
    private final Torrent torrent;

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
                this.torrent.announce + "?info_hash=" + this.torrent.infoHashBytes
                + "&peer_id=" + peerId + "&port=6881&uploaded=0&downloaded=0&left=" + this.torrent.length
                + "&compact=1"
        );

        HttpRequest request = HttpRequest.newBuilder(
                URI.create(full_request))
                .header("accept", "application/json")
                .build();

        System.out.println("request = " + request.toString());
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("status code: " + response.statusCode());
            System.out.println("headers: " + response.headers());
            System.out.println("response body: " + response.body());
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
