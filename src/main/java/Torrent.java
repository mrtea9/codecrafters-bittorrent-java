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

public class Torrent {
    private final Bencode bencode = new Bencode(false);
    private final String trackerPath;
    private final byte[] torrentData;
    private Map<String, Object> infoBytes;
    public String announce;
    public long length;
    public String infoHash;
    public long pieceLength;
    public byte[] infoHashBytes;

    public Torrent(String trackerPath) {
        this.trackerPath = trackerPath;
        this.torrentData = this.parseTorrent();
        getParams();
    }

    private void getParams() {
        Bencode bencode1 = new Bencode(true);
        this.infoBytes = (Map<String, Object>)bencode1.decode(this.torrentData, Type.DICTIONARY).get("info");

        Map<String, Object> decodedTorrent = decodeFile(this.torrentData);
        Map<String, Object> info = (Map<String, Object>)decodedTorrent.get("info");

        this.announce = (String)decodedTorrent.get("announce");
        this.length = (long)info.get("length");
        this.pieceLength = (long)info.get("piece length");

        getInfoHash(this.infoBytes, bencode1);
    }

    private void getInfoHash(Map<String, Object> info1, Bencode bencode1) {
        byte[] infoEncoded = bencode1.encode(info1);
        this.infoHash = bytesToHex(calculateHash(infoEncoded));
        this.infoHashBytes = calculateHash(infoEncoded);
    }

    public void printPieceHashes() {
        ByteBuffer piecesBuffer = (ByteBuffer)this.infoBytes.get("pieces");
        piecesBuffer.rewind();
        byte[] bytes = new byte[piecesBuffer.remaining()];
        System.out.println(bytesToHex(bytes));
    }

    private byte[] calculateHash(byte[] data) {
        byte[] hash = null;
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(data);
            hash = sha1.digest();
        } catch (NoSuchAlgorithmException e) {
            return e.getMessage().getBytes();
        }

        return hash;
    }

    public static String bytesToHex(byte[] hash) {
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
