import com.dampcake.bencode.Type;

import java.io.IOException;
import com.dampcake.bencode.Bencode;

import java.nio.charset.StandardCharsets;
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

        Map<String, Object> info1 =(Map<String, Object>)bencode1.decode(torrentData, Type.DICTIONARY).get("info");
        byte[] infoEncoded = bencode1.encode(info1);
        this.infoHash = calculateHash(infoEncoded);

        printPieceHashes(info);
    }

    private static void printPieceHashes(Map<?,?> infoDict) {
        var data = (String)infoDict.get("pieces");
        var bytes = data.getBytes(StandardCharsets.ISO_8859_1);
        System.out.print("Piece Hashes:");
        for(int i=0;i<bytes.length; ++i){
            if(i%20 == 0){
                System.out.println();
            }
            System.out.printf("%02x", bytes[i]);
        }
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
