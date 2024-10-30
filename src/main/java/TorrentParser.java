import com.dampcake.bencode.Type;
import com.google.gson.Gson;

import java.io.IOException;
import com.dampcake.bencode.Bencode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Map;

public class TorrentParser {
    private final Bencode bencode = new Bencode();
    private final String trackerPath;
    private final Map<String, Object> decodedTorrent;

    public TorrentParser(String trackerPath) {
        this.trackerPath = trackerPath;
        byte[] torrentData = parseTorrent();
        this.decodedTorrent = decodeFile(torrentData);
    }

    public Map<String, Object> getDecodedTorrent() {
        return this.decodedTorrent;
    }

    private Map<String, Object> decodeFile(byte[] torrentFile) {
        return bencode.decode(torrentFile, Type.DICTIONARY);
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

        return data;
    }
}
