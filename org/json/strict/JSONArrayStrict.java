package org.json.strict;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Extends JSONArray to accept only strictly JSON compatible representation.
 *
 * The role of this class is to validate correctness of the String used for
 * creation of JSON array. Useful if you want to build the JSON String yourself
 * and just check if it comply to JSON format.
 *
 * @author Konrad Borowiecki <konradborowiecki@gmail.com>
 */
public class JSONArrayStrict extends JSONArray {

    public JSONArrayStrict(JSONTokenerStrict x) throws JSONException {
        super();
        if (x.nextClean() != '[') {
            throw x.syntaxError("A JSONArray text must start with '['");
        }
        if (x.nextClean() != ']') {
            x.back();
            for (;;) {
                if (x.nextClean() == ',') {
                    x.back();
//                    this.myArrayList.add(JSONObject.NULL);
                    put(JSONObject.NULL);
                } else {
                    x.back();
//                    this.myArrayList.add(x.nextValue());
                    put(x.nextValue());
                }
                switch (x.nextClean()) {
                    case ',':
                        if (x.nextClean() == ']') {
                            return;
                        }
                        x.back();
                        break;
                    case ']':
                        return;
                    default:
                        throw x.syntaxError("Expected a ',' or ']'");
                }
            }
        }
    }

    public JSONArrayStrict(String source) throws JSONException {
        this(new JSONTokenerStrict(source));
    }
}
