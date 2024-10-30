import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class Bencoded {
    private static final Gson gson = new Gson();

    static String decodeBencode(String bencodedString) {
        if (Character.isDigit(bencodedString.charAt(0))) {
            return gson.toJson(decodeString(bencodedString));
        } else if (bencodedString.charAt(0) == 'i') {
            return gson.toJson(decodeNumber(bencodedString));
        } else if (bencodedString.charAt(0) == 'l') {
            return gson.toJson(decodeList(bencodedString));
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

    static List<String> decodeList(String bencodedString) {
        List<String> decodedList = new ArrayList<String>();
        if (bencodedString.equals("le")) return decodedList;
        int i = 1; // skipping the first l
        while (bencodedString.charAt(i) != 'e') {
            System.out.println(bencodedString.charAt(i));
            String element = decodeBencode(bencodedString.substring(i));
            System.out.println(element);
            i++;
        }
        System.out.println(bencodedString);
        decodedList.add("test");

        return decodedList;
    }
}
