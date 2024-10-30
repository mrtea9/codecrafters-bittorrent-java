import com.google.gson.Gson;

import java.util.*;


public class Bencoded {
    private static final Gson gson = new Gson();
    public String encodedString;

    public Bencoded(String encodedString) {
        this.encodedString = encodedString;
    }

    Object decodeBencode() {
        //System.out.println("encodedString = " + this.encodedString);
        char firstChar = this.encodedString.charAt(0);

        if (Character.isDigit(firstChar)) {
            return decodeString();
        } else if (firstChar == 'i') {
            return decodeNumber();
        } else if (firstChar == 'l') {
            return decodeList();
        } else if (firstChar == 'd') {
            return decodeDict();
        } else {
            throw new RuntimeException("Invalid bencoded value");
        }
    }

    private String decodeString() {
        int firstColonIndex = this.encodedString.indexOf(':');
        int length = Integer.parseInt(this.encodedString.substring(0, firstColonIndex));
        String result = this.encodedString.substring(firstColonIndex+1, firstColonIndex+1+length);

        this.encodedString = this.encodedString.substring(2 + length);

        //System.out.println("encoded string = " + this.encodedString);
        return result;
    }

    private long decodeNumber() {
        int lastCharIndex = this.encodedString.indexOf('e');
        long result = Long.parseLong(this.encodedString.substring(1, lastCharIndex));

        this.encodedString = this.encodedString.substring(lastCharIndex + 1);

        //System.out.println("encoded string = " + this.encodedString);
        return result;
    }

    private List<Object> decodeList() {
        List<Object> decodedList = new ArrayList<Object>();

        this.encodedString = this.encodedString.substring(1); // skip the l
        char firstChar = this.encodedString.charAt(0);
        while (firstChar != 'e'){
            Object element = decodeBencode();
            decodedList.add(element);

            firstChar = this.encodedString.charAt(0);
        }
        this.encodedString = this.encodedString.substring(1); // skip the e

        return decodedList;
    }

    private AbstractMap<String, Object> decodeDict() {
        AbstractMap<String, Object> decodedDict = new HashMap<>();

        this.encodedString = this.encodedString.substring(1); // skip the d
        char firstChar = this.encodedString.charAt(0);
        while (firstChar != 'e') {
            Object key = decodeBencode();
            Object value = decodeBencode();

            decodedDict.put(key.toString(), value);

            firstChar = this.encodedString.charAt(0);
        }
        this.encodedString = this.encodedString.substring(1); // skip the e

        return decodedDict;
    }
}
