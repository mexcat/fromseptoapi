package cl.getnet.jsontoapi.helpers;

import java.util.regex.Pattern;

public class SecurityValidator {
 //   private static final Pattern UUID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{1,100}$");
    private static final Pattern SAFE_SEGMENT = Pattern.compile("^[a-zA-Z0-9._-]{1,255}$");

    public static String safePathSegment(String segment, String fieldname){
        if(segment == null){
            throw new IllegalArgumentException(fieldname + ": null");
        }
        String trimmed = segment.trim();
        if(trimmed.isEmpty()){
            throw new IllegalArgumentException(fieldname + ": empty");
        }
        if( trimmed.contains("%")|| trimmed.contains("/")|| trimmed.contains(":")|| trimmed.contains("@")|| 
            trimmed.contains("?")|| trimmed.contains("&")|| trimmed.contains("\\")|| trimmed.contains("+")){
            throw new IllegalArgumentException(fieldname + ": ilegal characters");
        }
        if(!SAFE_SEGMENT.matcher(trimmed).matches()){
            throw new IllegalArgumentException(fieldname + ": ilegal characters ot too long");
        }
        return trimmed;
    }

}
