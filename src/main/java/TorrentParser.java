import com.dampcake.bencode.Type;

import java.io.IOException;
import com.dampcake.bencode.Bencode;

import java.nio.ByteBuffer;
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

    public TorrentParser(String trackerPath) {
        this.trackerPath = trackerPath;
        getParams();
    }

    private void getParams() {
        Bencode bencode1 = new Bencode(true);
        byte[] torrentData = this.parseTorrent();

        Map<String, Object> decodedTorrent = decodeFile(torrentData);
        Map<String, Object> info = (Map<String, Object>)decodedTorrent.get("info");
        System.out.println("decodedTorrent = " + decodedTorrent.toString());
        System.out.println("info = " + info.toString());

        this.announce = (String) decodedTorrent.get("announce");
        this.length = (long)info.get("length");
        this.pieceLength = (long)info.get("piece length");

        Map<String, Object> info1 =(Map<String, Object>)bencode1.decode(torrentData, Type.DICTIONARY).get("info");
        ByteBuffer piecesBuffer = (ByteBuffer)info1.get("pieces");
        System.out.println("info1 = " + info1.toString());
        piecesBuffer.rewind();
        byte[] bytes = new byte[piecesBuffer.remaining()];
        piecesBuffer.get(bytes);
        piecesBuffer.rewind();
        System.out.println(bytesToHex(bytes));

        byte[] infoEncoded = bencode1.encode(info1);
        this.infoHash = calculateHash(infoEncoded);
    }

    private static void printPieceHashes(String pieces) {
        byte[] bytes = pieces.getBytes();
        System.out.println(bytesToHex(bytes));
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

    private static String bytesToHex(byte[] hash) {
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
        Path path = Paths.get(this.trackerPath);
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
