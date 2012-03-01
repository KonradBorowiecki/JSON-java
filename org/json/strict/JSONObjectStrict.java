package org.json.strict;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Extends JSONObject to accept only strictly JSON compatible representation.
 *
 * The role of this class is to validate correctness of the String used for
 * creation of JSON object. Useful if you want to build the JSON String yourself
 * and just check if it comply to JSON format.
 *
 * @author Konrad Borowiecki <kb@geovs.com>
 */
public class JSONObjectStrict extends JSONObject {

    public JSONObjectStrict(JSONTokenerStrict x) throws JSONException {
        super();
        char c;
        String key;

        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }
        for (;;) {
            c = x.nextClean();
            switch (c) {
                case 0:
                    throw x.syntaxError("A JSONObject text must end with '}'");
                case '}':
                    return;
                default:
                    x.back();
                    key = x.nextValue().toString();
                    //the key must start with the quote (") character
                    if(c != '"')
                        throw x.syntaxError("Value used for key ("+key+") must be wrapped in quates e.g. (\"mykey\")");
            }
// The key is followed by ':'
            c = x.nextClean();
            if (c != ':') {
                throw x.syntaxError("Expected a ':' after a key");
            }
            this.putOnce(key, x.nextValue());
// Pairs are separated by ','
            switch (x.nextClean()) {
                case ',':
                    if (x.nextClean() == '}') {
                        return;
                    }
                    x.back();
                    break;
                case '}':
                    return;
                default:
                    throw x.syntaxError("Expected a ',' or '}'");
            }
        }
    }

    public JSONObjectStrict(String source) throws JSONException {
        this(new JSONTokenerStrict(source));
    }
}
