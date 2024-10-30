import com.google.gson.Gson;

import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.Type;
import java.util.Map;

public class Main {
  private static final Gson gson = new Gson();

  public static void main(String[] args) throws Exception {
    String command = args[0];
    if("decode".equals(command)) {
        String bencodedValue = args[1];
        String decoded;
        Bencode bencode = new Bencode();
        try {
             byte[] bencodedBytes = bencodedValue.getBytes();
             decoded = gson.toJson(bencode.decode(bencodedBytes, bencode.type(bencodedBytes)));
        } catch(RuntimeException e) {
          System.out.println(e.getMessage());
          return;
        }
        System.out.println(decoded);
    } else if ("info".equals(command)) {
        String trackerPath = args[1];
        TorrentParser torrent = new TorrentParser(trackerPath);
        Map<String, Object> decodedTorrent = torrent.getDecodedTorrent();
        Object announce = decodedTorrent.get("announce");
        System.out.println("Tracker URL: " + gson.toJson(announce));
        Object info = decodedTorrent.get("info");
        System.out.println(gson.toJson(info));
    } else {
      System.out.println("Unknown command: " + command);
    }

  }
  
}
