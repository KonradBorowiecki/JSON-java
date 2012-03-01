package org.json.strict;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Extends JSONTokener to override its nextValue() method to 
 * use strict equivalents of JSONObject and JSONArray. Also 
 * it only treats quote (") characters only as wrappers of string.
 *
 * The role of this class is to validate correctness of the String used for
 * creation of JSON object. Useful if you want to build the JSON String yourself
 * and just check if it comply to JSON format.
 *
 * @author Konrad Borowiecki <konradborowiecki@gmail.com>
 */
public class JSONTokenerStrict extends JSONTokener {

    public JSONTokenerStrict(String s) {
        super(s);
    }

    @Override
    public Object nextValue() throws JSONException {
        char c = this.nextClean();
        String string;
        
        switch (c) {
            case '"':
//            case '\'':
                return this.nextString(c);
            case '{':
                this.back();
                return new JSONObjectStrict(this);
            case '[':
                this.back();
                return new JSONArrayStrict(this);
        }
        /*
         * Handle unquoted text. This could be the values true, false, or null,
         * or it can be a number. An implementation (such as this one) is
         * allowed to also accept non-standard forms.
         *
         * Accumulate characters until we reach the end of the text or a
         * formatting character.
         */

        StringBuffer sb = new StringBuffer();
        while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
            sb.append(c);
            c = this.next();
        }
        this.back();

        string = sb.toString().trim();
        if ("".equals(string)) {
            throw this.syntaxError("Missing value");
        }
        return JSONObject.stringToValue(string);
    }
}
