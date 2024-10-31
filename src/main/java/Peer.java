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

        HttpRequest request = HttpRequest.newBuilder(
                URI.create(this.torrent.announce))
                .header("accept", "application/json")
                .build();

        System.out.println("request = " + request.toString());
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("response body: " + response.body());
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
