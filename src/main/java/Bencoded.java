import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class Bencoded {
    private static final Gson gson = new Gson();
    public String encodedString;

    public Bencoded(String encodedString) {
        this.encodedString = encodedString;
    }

    Object decodeBencode() {
        System.out.println("encodedString = " + this.encodedString);

        if (Character.isDigit(this.encodedString.charAt(0))) {
            return decodeString();
        } else if (this.encodedString.charAt(0) == 'i') {
            return decodeNumber();
        } else if (this.encodedString.charAt(0) == 'l') {
            return decodeList();
        } else {
            throw new RuntimeException("Only strings are supported at the moment");
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
        while (this.encodedString.charAt(0) != 'e'){
            Object element = decodeBencode();
            //System.out.println("element = " + element);
            //System.out.println("encodedString = " + this.encodedString);
            decodedList.add(element);
            //System.out.println("decodedList = " + decodedList.toString());
        }
        this.encodedString = this.encodedString.substring(1); // skip the e
        //System.out.println("encodedString end = " + this.encodedString);

        return decodedList;
    }
}
