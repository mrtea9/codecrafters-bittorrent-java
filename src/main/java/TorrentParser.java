import com.dampcake.bencode.Type;
import com.google.gson.Gson;

import java.io.IOException;
import com.dampcake.bencode.Bencode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Map;
import java.security.MessageDigest;

public class TorrentParser {
    private final Bencode bencode = new Bencode();
    private final String trackerPath;
    private final Map<String, Object> decodedTorrent;
    public String announce;
    public long length;

    public TorrentParser(String trackerPath) {
        this.trackerPath = trackerPath;
        byte[] torrentData = parseTorrent();
        this.decodedTorrent = decodeFile(torrentData);

        Map<String, Object> info = (Map<String, Object>) this.decodedTorrent.get("info");

        this.announce = (String)this.decodedTorrent.get("announce");
        this.length = (long)info.get("length");


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
