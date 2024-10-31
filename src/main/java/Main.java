import com.google.gson.Gson;

import com.dampcake.bencode.Bencode;

import java.net.http.HttpClient;

public class Main {
  private static final Gson gson = new Gson();

  public static void main(String[] args) throws Exception {
    processInput(args);
  }

  private static void processInput(String[] args) {
      String command = args[0];
      if("decode".equals(command)) {
          String bencodedValue = args[1];
          String decoded = getDecoded(bencodedValue);

          System.out.println(decoded);
      } else if ("info".equals(command)) {
          String trackerPath = args[1];
          Torrent torrent = new Torrent(trackerPath);

          printInfo(torrent);
      } else if ("peers".equals(command)) {
          String trackerPath = args[1];
          Torrent torrent = new Torrent(trackerPath);
          Peer peer = new Peer(torrent);
          System.out.println(peer.discoverPeers());
      }
      else {
          System.out.println("Unknown command: " + command);
      }
  }

  private static void printInfo(Torrent torrent) {
      System.out.println("Tracker URL: " + torrent.announce);
      System.out.println("Length: " + torrent.length);
      System.out.println("Info Hash: " + torrent.infoHash);
      System.out.println("Piece Length: " + torrent.pieceLength);
      torrent.printPieceHashes();
  }

  private static String getDecoded(String bencodedValue) {
      Bencode bencode = new Bencode();
      String decoded;
      try {
          byte[] bencodedBytes = bencodedValue.getBytes();
          decoded = gson.toJson(bencode.decode(bencodedBytes, bencode.type(bencodedBytes)));
      } catch(RuntimeException e) {
          return e.getMessage();
      }
      return decoded;
  }
}
