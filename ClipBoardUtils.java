public class ClipboardUtils {

    private ClipboardUtils() {}

    /**
     * This method copy text to the clipboard
     * @param text: text to be copy
     */
    public static void copyText(CharSequence text) {

        ClipboardManager clipboard = (ClipboardManager) Utils.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("text", text));
    }

    /**
     * This method get text from clipboard, is like "PASTE"
     */
    public static CharSequence getText() {

        ClipboardManager clipboard = (ClipboardManager) Utils.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(Utils.getContext());
        }
        return null;
    }

    /**
     * This method copy in clipboard an Uri
     * @param uri: Urito be copy
     */
    public static void copyUri(Uri uri) {

        ClipboardManager clipboard = (ClipboardManager) Utils.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newUri(Utils.getContext().getContentResolver(), "uri", uri));
    }

    /**
     * This method get Uri from clipboard
     */
    public static Uri getUri() {

        ClipboardManager clipboard = (ClipboardManager) Utils.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getUri();
        }
        return null;
    }

    /**
     * This method copy an intent into the clipboard
     * @param intent: Intent to be copy
     */
    public static void copyIntent(Intent intent) {

        ClipboardManager clipboard = (ClipboardManager) Utils.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newIntent("intent", intent));
    }

    /**
     * This method get Intent from clipboard
     */
    public static Intent getIntent() {
        ClipboardManager clipboard = (ClipboardManager) Utils.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getIntent();
        }
        return null;
    }
}