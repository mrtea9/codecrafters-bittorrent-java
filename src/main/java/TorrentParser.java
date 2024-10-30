import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Base64;

public class TorrentParser {
    private static final Gson gson = new Gson();
    private String trackerPath;
    private byte[] torrentData;
    private Object decodedTorrent;
    private Bencoded bencoded;

    public TorrentParser(String trackerPath) {
        this.trackerPath = trackerPath;
        this.torrentData = parseTorrent();
        this.decodedTorrent = bencoded.decodeFile(torrentData);

    }

    private byte[] parseTorrent() {
        Path path = Paths.get(trackerPath);
        byte[] data = null;

        try {
            data = Files.readAllBytes(path);
        } catch(IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        //System.out.println(Base64.getEncoder().encodeToString(data));
        return data;
        //return new String(data, StandardCharsets.UTF_8);
        //return Base64.getEncoder().encodeToString(data);
    }
}
