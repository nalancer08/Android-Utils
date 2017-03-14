public class ConvertUtils {

    private ConvertUtils() {}

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * This method convert bytes into hex string
     * @param bytes : byte[] to be tranform
     */
    public static String bytesToHexString(byte[] bytes) {

        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    /**
     * This method convert hex string into bytes
     * @param hexString: String to be transform
     */
    public static byte[] hexStringToBytes(String hexString) {

        if (StringUtils.isSpace(hexString)) return null;
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hexToDec(hexBytes[i]) << 4 | hexToDec(hexBytes[i + 1]));
        }
        return ret;
    }

    /**
     * This method convert char text into decimal
     * @param hexChar: Text char
     */
    private static int hexToDec(char hexChar) {

        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * This method convert chars into byte[]
     * @param chars: Chars
     */
    public static byte[] charsToBytes(char[] chars) {

        if (chars == null || chars.length <= 0) return null;
        int len = chars.length;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (chars[i]);
        }
        return bytes;
    }

    /**
     * This method convert byte[] to chars
     * @param bytes: byte[] to be transform
     */
    public static char[] bytesToChars(byte[] bytes) {

        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) (bytes[i] & 0xff);
        }
        return chars;
    }

    /**
     * This method convert byte[] to bits
     * @param bytes: byte[] to be transform
     */
    public static String bytesToBits(byte[] bytes) {

        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            for (int j = 7; j >= 0; --j) {
                sb.append(((aByte >> j) & 0x01) == 0 ? '0' : '1');
            }
        }
        return sb.toString();
    }

    /**
     * This emthod convert bits into byte[]
     * @param bits: String of bits
     */
    public static byte[] bitsTpBytes(String bits) {

        int lenMod = bits.length() % 8;
        int byteLen = bits.length() / 8;
        // 不是8的倍数前面补0
        if (lenMod != 0) {
            for (int i = lenMod; i < 8; i++) {
                bits = "0" + bits;
            }
            byteLen++;
        }
        byte[] bytes = new byte[byteLen];
        for (int i = 0; i < byteLen; ++i) {
            for (int j = 0; j < 8; ++j) {
                bytes[i] <<= 1;
                bytes[i] |= bits.charAt(i * 8 + j) - '0';
            }
        }
        return bytes;
    }

    /**
     * This method convert bitmap to ytes
     *
     * @param bitmap: Bitmap to be transform
     * @param format: Compress format
     */
    public static byte[] bitmapToBytes(Bitmap bitmap, Bitmap.CompressFormat format) {

        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * This method convert byte[] to bitmap
     * @param bytes: byte[] with data of image to be convert
     */
    public static Bitmap bytes2Bitmap(byte[] bytes) {

        return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * This method convert drawable to bitmap
     * @param drawable: Drawable to transform
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {

        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * This method convert bitmap to drawable
     * @param res: Resources to save
     * @param bitmap: Bitmap to be convert
     */
    public static Drawable bitmapToDrawable(Resources res, Bitmap bitmap) {

        return bitmap == null ? null : new BitmapDrawable(res, bitmap);
    }

    /**
     * This method convert drawable to byte[]
     * @param drawable: Drawable
     * @param format: Compress format
     */
    public static byte[] drawableToBytes(Drawable drawable, Bitmap.CompressFormat format) {

        return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), format);
    }

    /**
     * This method convert byte[] to drawable
     * @param res: Resources to be save
     * @param bytes: byte[] with the data to be convert
     */
    public static Drawable bytes2Drawable(Resources res, byte[] bytes) {

        return res == null ? null : bitmap2Drawable(res, bytes2Bitmap(bytes));
    }

    /**
     * This method convert view to bitmap
     * @param view: View to be transform
     */
    public static Bitmap viewToBitmap(View view) {

        if (view == null) return null;
        Bitmap ret = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ret);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }

    /**
     * This method convert dp to px
     * @param dpValue: DP value to be transform
     */
    public static int dpTopx(float dpValue) {

        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * This method convert px to dp
     * @param pxValue: Pixel value to be transfor
     */
    public static int pxTodp(float pxValue) {

        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * This method convert sp to px
     * @param spValue: Sp value to convert
     */
    public static int spTopx(float spValue) {
        final float fontScale = Utils.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * This method convert px to sp
     * @param pxValue: Pixel value to be converted
     */
    public static int pxTosp(float pxValue) {
        
        final float fontScale = Utils.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}