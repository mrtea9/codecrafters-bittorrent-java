import com.google.gson.Gson;

import com.dampcake.bencode.Bencode;


public class Main {
  private static final Gson gson = new Gson();

  public static void main(String[] args) throws Exception {
      String command = args[0];
      if("decode".equals(command)) {
          String bencodedValue = args[1];

          processDecode(bencodedValue);
      } else if ("info".equals(command)) {
          String trackerPath = args[1];

          processInfo(trackerPath);
      } else if ("peers".equals(command)) {
          String trackerPath = args[1];

          processPeers(trackerPath);
      } else if ("handshake".equals(command)) {
          String trackerPath = args[1];
          String address = args[2];

          processHandshake(trackerPath, address);
      } else if ("download_piece".equals(command)) {
          String fileToCreate = args[2];
          String trackerPath = args[3];
          int pieceIndex = Integer.parseInt(args[4]);

          processDownloadPiece(fileToCreate, trackerPath, pieceIndex);
      }
      else {
          System.out.println("Unknown command: " + command);
      }
  }

  private static void processDecode(String bencodedValue) {
      String decoded = getDecoded(bencodedValue);

      System.out.println(decoded);
  }

  private static void processInfo(String trackerPath) {
      Torrent torrent = new Torrent(trackerPath);

      printInfo(torrent);
  }

  private static void processPeers(String trackerPath) {
      Torrent torrent = new Torrent(trackerPath);
      Peer peer = new Peer(torrent);

      peer.performGetRequest();
  }

  private static void processHandshake(String trackerPath, String address) {
      Torrent torrent = new Torrent(trackerPath);

      PeerConnection peerConnection = new PeerConnection(address, torrent);
      peerConnection.creatingConnection();
  }

  private static void processDownloadPiece(String fileToCreate, String trackerPath, int pieceIndex) {
      Torrent torrent = new Torrent(trackerPath);
      DownloaderPiece downloaderPiece = new DownloaderPiece(torrent, fileToCreate, pieceIndex);

  }

  private static void printInfo(Torrent torrent) {
      System.out.println("Tracker URL: " + torrent.announce);
      System.out.println("Length: " + torrent.length);
      System.out.println("Info Hash: " + torrent.infoHash);
      System.out.println("Piece Length: " + torrent.pieceLength);
      torrent.printPieceHashes();
  }

  public static String getDecoded(String bencodedValue) {
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
