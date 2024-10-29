import com.google.gson.Gson;


public class Bencoded {
    private static final Gson gson = new Gson();

    static String decodeBencode(String bencodedString) {
        if (Character.isDigit(bencodedString.charAt(0))) {
            return gson.toJson(decodeString(bencodedString));
        } else if (bencodedString.charAt(0) == 'i') {
            return gson.toJson(decodeNumber(bencodedString));
        } else {
            throw new RuntimeException("Only strings are supported at the moment");
        }
    }

    static String decodeString(String bencodedString) {
        int firstColonIndex = bencodedString.indexOf(':');
        int length = Integer.parseInt(bencodedString.substring(0, firstColonIndex));
        return bencodedString.substring(firstColonIndex+1, firstColonIndex+1+length);
    }

    static long decodeNumber(String bencodedString) {
        int lastCharIndex = bencodedString.indexOf('e');
        return Long.parseLong(bencodedString.substring(1, lastCharIndex));
    }
}
