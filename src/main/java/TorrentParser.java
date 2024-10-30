import com.dampcake.bencode.Type;
import com.google.gson.Gson;

import java.io.IOException;
import com.dampcake.bencode.Bencode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;

public class TorrentParser {
    private static final Gson gson = new Gson();
    private String trackerPath;
    private byte[] torrentData;
    private Map<String, Object> decodedTorrent;
    private Bencode bencode = new Bencode();

    public TorrentParser(String trackerPath) {
        this.trackerPath = trackerPath;
        this.torrentData = parseTorrent();
        this.decodedTorrent = decodeFile(torrentData);

        System.out.println(gson.toJson(decodedTorrent));
    }

    public Map<String, Object> decodeFile(byte[] torrentFile) {
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
