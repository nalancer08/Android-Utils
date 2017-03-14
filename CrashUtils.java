public class CrashUtils implements Thread.UncaughtExceptionHandler {

    private volatile static CrashUtils mInstance;

    private UncaughtExceptionHandler mHandler;

    private boolean mInitialized;
    private String  crashDir;
    private String  versionName;
    private int     versionCode;

    private CrashUtils() {
    }

    /**
     * This method get the instnace of the class (singleton)
     */
    public static CrashUtils getInstance() {

        if (mInstance == null) {
            synchronized (CrashUtils.class) {
                if (mInstance == null) {
                    mInstance = new CrashUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * This method init the class
     */
    public boolean init() {

        if (mInitialized) return true;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File baseCache = Utils.getContext().getExternalCacheDir();
            if (baseCache == null) return false;
            crashDir = baseCache.getPath() + File.separator + "crash" + File.separator;
        } else {
            File baseCache = Utils.getContext().getCacheDir();
            if (baseCache == null) return false;
            crashDir = baseCache.getPath() + File.separator + "crash" + File.separator;
        }
        try {
            PackageInfo pi = Utils.getContext().getPackageManager().getPackageInfo(Utils.getContext().getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        return mInitialized = true;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        String now = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        final String fullPath = crashDir + now + ".txt";
        if (!createOrExistsFile(fullPath)) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter(fullPath, false));
                    pw.write(getCrashHead());
                    throwable.printStackTrace(pw);
                    Throwable cause = throwable.getCause();
                    while (cause != null) {
                        cause.printStackTrace(pw);
                        cause = cause.getCause();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    CloseUtils.closeIO(pw);
                }
            }
        }).start();
        if (mHandler != null) {
            mHandler.uncaughtException(thread, throwable);
        }
    }

    /**
     * This method print when app crashes
     */
    private String getCrashHead() {

        return "\n************* Crash Log Head ****************" +
            "\nDevice Manufacturer: " + Build.MANUFACTURER +
            "\nDevice Model       : " + Build.MODEL +
            "\nAndroid Version    : " + Build.VERSION.RELEASE +
            "\nAndroid SDK        : " + Build.VERSION.SDK_INT +
            "\nApp VersionName    : " + versionName +
            "\nApp VersionCode    : " + versionCode +
            "\n************* Crash Log Head ****************\n\n";
    }

    /**
     * This method create or check a path to save the log
     * @param filePath: File path to save log
     */
    private static boolean createOrExistsFile(String filePath) {

        File file = new File(filePath);
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * This method create or check a path to save the log 
     * @param filePath: File to save log
     */
    private static boolean createOrExistsDir(File file) {
        
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }
}