import com.dampcake.bencode.Type;

import java.io.IOException;
import com.dampcake.bencode.Bencode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.security.MessageDigest;

public class TorrentParser {
    private final Bencode bencode = new Bencode(false);
    private final String trackerPath;
    public String announce;
    public long length;
    public String infoHash;
    public long pieceLength;
    public String piecesHash;

    public TorrentParser(String trackerPath) {
        Bencode bencode1 = new Bencode(true);
        this.trackerPath = trackerPath;
        byte[] torrentData = parseTorrent();

        Map<String, Object> decodedTorrent = decodeFile(torrentData);
        Map<String, Object> info = (Map<String, Object>)decodedTorrent.get("info");

        this.announce = (String) decodedTorrent.get("announce");
        this.length = (long)info.get("length");

        byte[] infoEncoded = bencode1.encode((Map<String, Object>)bencode1.decode(torrentData, Type.DICTIONARY).get("info"));
        this.infoHash = calculateHash(infoEncoded);

        this.pieceLength = (long)info.get("piece length");
        this.piecesHash = (String)info.get("pieces");
        System.out.println(this.piecesHash);
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

        return bytesToHex(hash);
    }

    private String bytesToHex(byte[] hash) {
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
