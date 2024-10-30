import com.dampcake.bencode.Type;
import com.google.gson.Gson;

import java.io.IOException;
import com.dampcake.bencode.Bencode;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.security.MessageDigest;

public class TorrentParser {
    private final Bencode bencode = new Bencode();
    private final String trackerPath;
    private final Map<String, Object> decodedTorrent;
    public String announce;
    public long length;
    public String infoHash;

    public TorrentParser(String trackerPath) {
        this.trackerPath = trackerPath;
        byte[] torrentData = parseTorrent();
        this.decodedTorrent = decodeFile(torrentData);

        Map<String, Object> info = (Map<String, Object>) this.decodedTorrent.get("info");
        byte[] infoEncoded = bencode.encode(info);

        this.announce = (String)this.decodedTorrent.get("announce");
        this.length = (long)info.get("length");

        this.infoHash = calculateHash(infoEncoded);
    }

    private String calculateHash(byte[] data) {
        byte[] hash = null;
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(data);
            hash = sha1.digest();
        } catch (NoSuchAlgorithmException e) {
            return e.getMessage();
        }

        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
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
