import javax.imageio.IIOException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Arrays;

public class TorrentParser {
    private String trackerPath;
    private String torrentData;
    private Object decodedTorrent;
    private Bencoded bencoded;

    public TorrentParser(String trackerPath) {
        this.trackerPath = trackerPath;
        this.torrentData = parseTorrent();
        this.bencoded = new Bencoded(this.torrentData);
        this.decodedTorrent = bencoded.decodeBencode();

        System.out.println(this.decodedTorrent);
    }

    private String parseTorrent() {
        Path path = Paths.get(trackerPath);
        byte[] data;

        try {
            data = Files.readAllBytes(path);
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return "error";
        }

        return new String(data, StandardCharsets.UTF_8);
    }
}
