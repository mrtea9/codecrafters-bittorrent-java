import com.google.gson.Gson;
// import com.dampcake.bencode.Bencode; - available if you need it!

public class Main {
  private static final Gson gson = new Gson();

  public static void main(String[] args) throws Exception {
    String command = args[0];
    if("decode".equals(command)) {
        String bencodedValue = args[1];
        String decoded;
        Bencoded bencoded = new Bencoded(bencodedValue);
        try {
          decoded = gson.toJson(bencoded.decodeBencode());
        } catch(RuntimeException e) {
          System.out.println(e.getMessage());
          return;
        }
        System.out.println(decoded.toString());
    } else {
      System.out.println("Unknown command: " + command);
    }

  }
  
}
