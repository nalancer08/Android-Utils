public class EncodeUtils {

    private EncodeUtils() {}

    /**
     * This method encode a url with default UFT8 charset
     * @param input: Url to be encode
     */
    public static String urlEncode(String input) {

        return urlEncode(input, "UTF-8");
    }

    /**
     * This mehtod encode a url with a custom charset
     * @param input: Url to be encode
     * @param charset: Charset to apply in encode
     */
    public static String urlEncode(String input, String charset) {

        try {
            return URLEncoder.encode(input, charset);
        } catch (UnsupportedEncodingException e) {
            return input;
        }
    }

    /**
     * This method decode a url with default UTF8 charset
     * @param input: Url to be decode
     */
    public static String urlDecode(String input) {

        return urlDecode(input, "UTF-8");
    }

    /**
     * This emthod decode a url with a custom charset
     * @param input: Url to be decode
     * @param charset: Charset to apply in decode
     */
    public static String urlDecode(String input, String charset) {

        try {
            return URLDecoder.decode(input, charset);
        } catch (UnsupportedEncodingException e) {
            return input;
        }
    }

    /**
     * This method encode a String into base64
     * @param input: String to be encode
     */
    public static byte[] base64Encode(String input) {

        return base64Encode(input.getBytes());
    }

    /**
     * This method encode a byte[] into base64
     * @param input: byte[] to be encode
     */
    public static byte[] base64Encode(byte[] input) {

        return Base64.encode(input, Base64.NO_WRAP);
    }

    /**
     * This method encode a byte[] into base64 and return a String with codification
     * @param input: byte[] to be encode
     */
    public static String base64EncodeToString(byte[] input) {

        return Base64.encodeToString(input, Base64.NO_WRAP);
    }

    /**
     * This method decode a base64 String
     * @param input: String to be decode
     */
    public static byte[] base64Decode(String input) {

        return Base64.decode(input, Base64.NO_WRAP);
    }

    /**
     * This method decode a base64 byte[] 
     * @param input: byte[] to be decode
     */
    public static byte[] base64Decode(byte[] input) {

        return Base64.decode(input, Base64.NO_WRAP);
    }

    /**
     * This method encode html
     * @param input: CharSequence to be encode
     */
    public static String htmlEncode(CharSequence input) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return Html.escapeHtml(input);
        } else {
            StringBuilder out = new StringBuilder();
            for (int i = 0, len = input.length(); i < len; i++) {
                char c = input.charAt(i);
                if (c == '<') {
                    out.append("&lt;");
                } else if (c == '>') {
                    out.append("&gt;");
                } else if (c == '&') {
                    out.append("&amp;");
                } else if (c >= 0xD800 && c <= 0xDFFF) {
                    if (c < 0xDC00 && i + 1 < len) {
                        char d = input.charAt(i + 1);
                        if (d >= 0xDC00 && d <= 0xDFFF) {
                            i++;
                            int codepoint = 0x010000 | (int) c - 0xD800 << 10 | (int) d - 0xDC00;
                            out.append("&#").append(codepoint).append(";");
                        }
                    }
                } else if (c > 0x7E || c < ' ') {
                    out.append("&#").append((int) c).append(";");
                } else if (c == ' ') {
                    while (i + 1 < len && input.charAt(i + 1) == ' ') {
                        out.append("&nbsp;");
                        i++;
                    }
                    out.append(' ');
                } else {
                    out.append(c);
                }
            }
            return out.toString();
        }
    }

    /**
     * This method decode html into CharSequence
     * @param input: String with html to be decode
     */
    @SuppressWarnings("deprecation")
    public static CharSequence htmlDecode(String input) {
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }
    }
}