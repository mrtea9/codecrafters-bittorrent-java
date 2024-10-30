import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class Bencoded {
    private static final Gson gson = new Gson();
    private String encodedString;

    public Bencoded(String encodedString) {
        this.encodedString = encodedString;
    }

    String decodeBencode() {
        if (Character.isDigit(encodedString.charAt(0))) {
            return gson.toJson(decodeString(encodedString));
        } else if (encodedString.charAt(0) == 'i') {
            return gson.toJson(decodeNumber(encodedString));
        } else if (encodedString.charAt(0) == 'l') {
            return gson.toJson(decodeList(encodedString));
        } else {
            throw new RuntimeException("Only strings are supported at the moment");
        }
    }

    private String decodeString(String bencodedString) {
        int firstColonIndex = bencodedString.indexOf(':');
        int length = Integer.parseInt(bencodedString.substring(0, firstColonIndex));
        this.encodedString = bencodedString.substring(2 + length);
        return bencodedString.substring(firstColonIndex+1, firstColonIndex+1+length);
    }

    private long decodeNumber(String bencodedString) {
        int lastCharIndex = bencodedString.indexOf('e');
        this.encodedString = bencodedString.substring(lastCharIndex);
        return Long.parseLong(bencodedString.substring(1, lastCharIndex));
    }

    private List<String> decodeList(String bencodedString) {
        List<String> decodedList = new ArrayList<String>();
        if (bencodedString.equals("le")) return decodedList;
        int i = 1; // skipping the first l
        while (bencodedString.charAt(i) != 'e') {
            System.out.println("bencoded = " + bencodedString);
            System.out.println("char at " + i + " = " + bencodedString.charAt(i));
            String element = decodeBencode();
            System.out.println("element = " + element);
            i++;
        }
        System.out.println("sad = " + bencodedString);
        decodedList.add("test");

        return decodedList;
    }
}
