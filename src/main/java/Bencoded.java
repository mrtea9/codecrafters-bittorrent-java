import com.dampcake.bencode.Type;
import com.google.gson.Gson;

import java.util.*;
import com.dampcake.bencode.Bencode;


public class Bencoded {
    private static final Gson gson = new Gson();
    private static final Bencode bencode = new Bencode();
    public String encodedString;

    public Bencoded(String encodedString) {
        this.encodedString = encodedString;
    }

    public Object decodeBencode() {
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

    public Map<String, Object> decodeFile(byte[] torrentFile) {
        final Map<String, Object> torrentFileDecoded =
                bencode.decode(torrentFile, Type.DICTIONARY);
        System.out.println(gson.toJson(torrentFileDecoded));
        return bencode.decode(torrentFile, Type.DICTIONARY);
    }

    private String decodeString() {
        System.out.println("encoded string len = " + this.encodedString.length());
        int firstColonIndex = this.encodedString.indexOf(':');
        System.out.println("firstColonIndex = " + firstColonIndex);
        int length = Integer.parseInt(this.encodedString.substring(0, firstColonIndex));
        System.out.println("length = " + length);
        int numDigits = Integer.toString(length).length();
        System.out.println("numDigits = " + numDigits);
        String result = this.encodedString.substring(firstColonIndex + 1, firstColonIndex + 1 + length);
        System.out.println("result = " + result);
        this.encodedString = this.encodedString.substring(numDigits + 1 + length);

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
            System.out.println("key = " + key.toString());
            System.out.println("value = " + value.toString());


            decodedDict.put(key.toString(), value);
            System.out.println("decodedDict = " + decodedDict.toString());
            firstChar = this.encodedString.charAt(0);
        }
        this.encodedString = this.encodedString.substring(1); // skip the e

        return decodedDict;
    }
}
