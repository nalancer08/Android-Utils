public class AppUtils {

    private AppUtils() {}

    /**
     * This method return if an app it's installed
     * @param context: Application context
     * @param packageName: String with the package name
     */
    public static boolean isInstallApp(Context context, String packageName) {

        return !StringUtils.isSpace(packageName) && IntentUtils.getLaunchAppIntent(packageName) != null;
    }

    /**
     * This method install an app with the file path
     * @param context: Application context
     * @param filePath: Application path
     */
    public static void installApp(Context context, String filePath) {

        installApp(context, FileUtils.getFileByPath(filePath));
    }

    /**
     * This method install an app with the file
     * @param context: Application context
     * @param file: Application file
     */
    public static void installApp(Context context, File file) {

        if (!FileUtils.isFileExists(file)) return;
        context.startActivity(IntentUtils.getInstallAppIntent(file));
    }

    /**
     * This method install an app with the file by path
     * @param activity: Activity
     * @param filePath: File path
     * @param requestCode: Code for request
     */
    public static void installApp(Activity activity, String filePath, int requestCode) {

        installApp(activity, FileUtils.getFileByPath(filePath), requestCode);
    }

    /**
     * This method install an app with the file by path
     * @param activity: Activity
     * @param file: File
     * @param requestCode: Code for request
     */
    public static void installApp(Activity activity, File file, int requestCode) {

        if (!FileUtils.isFileExists(file)) return;
        activity.startActivityForResult(IntentUtils.getInstallAppIntent(file), requestCode);
    }

    /**
     * This method install ann app in silent mode, and return the status of the installation
     * @param filePath: Thi file path of the app
     */
    public static boolean installAppSilent(String filePath) {

        File file = FileUtils.getFileByPath(filePath);
        if (!FileUtils.isFileExists(file)) return false;
        String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install " + filePath;
        ShellUtils.CommandResult commandResult = ShellUtils.execCmd(command, !isSystemApp(Utils.getContext()), true);
        return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success");
    }

    /**
     * This method uninstall ann app
     * @param context: Application context
     * @param packageName: Application package name
     */
    public static void uninstallApp(Context context, String packageName) {

        if (StringUtils.isSpace(packageName)) return;
        context.startActivity(IntentUtils.getUninstallAppIntent(packageName));
    }

    /**
     * This method uninstall ann app with a request code
     * @param activity: Activity
     * @param packageName: Tha application package name
     * @param requestCode: Request ode
     */
    public static void uninstallApp(Activity activity, String packageName, int requestCode) {

        if (StringUtils.isSpace(packageName)) return;
        activity.startActivityForResult(IntentUtils.getUninstallAppIntent(packageName), requestCode);
    }

    /**
     * This method uninstall ann app in silent mode
     * @param context: Application context
     * @param packageName: The application package name
     * @param isKeepData: Flag to keep data
     */
    public static boolean uninstallAppSilent(Context context, String packageName, boolean isKeepData) {

        if (StringUtils.isSpace(packageName)) return false;
        String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall " + (isKeepData ? "-k " : "") + packageName;
        ShellUtils.CommandResult commandResult = ShellUtils.execCmd(command, !isSystemApp(context), true);
        return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success");
    }

    /**
     * This method check if the app need root to be installed
     */
    public static boolean isAppRoot() {

        ShellUtils.CommandResult result = ShellUtils.execCmd("echo root", true);
        if (result.result == 0) {
            return true;
        }
        if (result.errorMsg != null) {
            LogUtils.d("isAppRoot", result.errorMsg);
        }
        return false;
    }

    /**
     * This method lauch ann app
     * @param packageName: Application package name
     */
    public static void launchApp(String packageName) {

        if (StringUtils.isSpace(packageName)) return;
        Utils.getContext().startActivity(IntentUtils.getLaunchAppIntent(packageName));
    }

    /**
     * This method lauch an app with request code
     * @param activity: Activity
     * @param packageName: Application package name
     * @param requestCode: Request code
     */
    public static void launchApp(Activity activity, String packageName, int requestCode) {

        if (StringUtils.isSpace(packageName)) return;
        activity.startActivityForResult(IntentUtils.getLaunchAppIntent(packageName), requestCode);
    }

    /**
     * This mehtod get the app name
     * @param context: Application context
     * @param packageName: Application package name
     */
    public static String getAppName(Context context, String packageName) {

        if (StringUtils.isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method get the app icon from current app
     * @param context: Application context
     */
    public static Drawable getAppIcon(Context context) {
        
        return getAppIcon(context, context.getPackageName());
    }

    /**
     * This method get application icon with the package name
     * @param context: Application context
     * @param packageName: Application package name
     */
    public static Drawable getAppIcon(Context context, String packageName) {

        if (StringUtils.isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method get the application path from currrent app
     * @param context: Application context
     */
    public static String getAppPath(Context context) {

        return getAppPath(context, context.getPackageName());
    }

    /**
     * This method get application path
     * @param context: Application context
     * @param packageName: Application package name
     */
    public static String getAppPath(Context context, String packageName) {

        if (StringUtils.isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method get current version name app
     * @param context: Application context
     */
    public static String getAppVersionName(Context context) {

        return getAppVersionName(context, context.getPackageName());
    }

    /**
     * This method get version name of the application requested
     * @param context: Application context
     * @param packageName: Application package name
     */
    public static String getAppVersionName(Context context, String packageName) {

        if (StringUtils.isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method get current version app
     * @param context: Application context
     */
    public static int getAppVersionCode(Context context) {

        return getAppVersionCode(context, context.getPackageName());
    }

    /**
     * This method get version of the application requested
     * @param context: Application context
     * @param packageName: Application package name
     */
    public static int getAppVersionCode(Context context, String packageName) {

        if (StringUtils.isSpace(packageName)) return -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * This method return if the current app is a system app
     * @param context: Application context
     */
    public static boolean isSystemApp(Context context) {

        return isSystemApp(context, context.getPackageName());
    }

    /**
     *      * This method return if the app is a system app
     * @param context: Application context
     * @param packageName: aPplication package name
     */
    public static boolean isSystemApp(Context context, String packageName) {

        if (StringUtils.isSpace(packageName)) return false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This class get information of current app
     */
    public static class AppInfo {

        private String name;
        private Drawable icon;
        private String packageName;
        private String packagePath;
        private String versionName;
        private int versionCode;
        private boolean isSystem;

        public Drawable getIcon() {

            return icon;
        }

        public void setIcon(Drawable icon) {

            this.icon = icon;
        }

        public boolean isSystem() {

            return isSystem;
        }

        public void setSystem(boolean isSystem) {

            this.isSystem = isSystem;
        }

        public String getName() {

            return name;
        }

        public void setName(String name) {

            this.name = name;
        }

        public String getPackageName() {

            return packageName;
        }

        public void setPackageName(String packagName) {

            this.packageName = packagName;
        }

        public String getPackagePath() {

            return packagePath;
        }

        public void setPackagePath(String packagePath) {

            this.packagePath = packagePath;
        }

        public int getVersionCode() {

            return versionCode;
        }

        public void setVersionCode(int versionCode) {

            this.versionCode = versionCode;
        }

        public String getVersionName() {

            return versionName;
        }

        public void setVersionName(String versionName) {

            this.versionName = versionName;
        }

        /**
         * @param name        
         * @param icon        
         * @param packageName 
         * @param packagePath 
         * @param versionName 
         * @param versionCode 
         * @param isSystem    
         */
        public AppInfo(String packageName, String name, Drawable icon, String packagePath, String versionName, int versionCode, boolean isSystem) {

            this.setName(name);
            this.setIcon(icon);
            this.setPackageName(packageName);
            this.setPackagePath(packagePath);
            this.setVersionName(versionName);
            this.setVersionCode(versionCode);
            this.setSystem(isSystem);
        }

        @Override
        public String toString() {

            return "App package name: " + getPackageName() +
                    "\nApp name：" + getName() +
                    "\nApp icon：" + getIcon() +
                    "\nApp pakcage path：" + getPackagePath() +
                    "\nApp vresion anem：" + getVersionName() +
                    "\nApp version code：" + getVersionCode() +
                    "\nApp system：" + isSystem();
        }
    }

    /**
     * This mehtod return current app info object
     * @param context: Application context
     */
    public static AppInfo getAppInfo(Context context) {

        return getAppInfo(context, context.getPackageName());
    }

    /**
     * This mehtod return app info object
     * @param context: Application context
     * @param packageName: Application package name
     */
    public static AppInfo getAppInfo(Context context, String packageName) {

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return getBean(pm, pi);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method get object AppInfo
     *
     * @param pm: PackagerManager
     * @param pi: PackageInfo
     */
    private static AppInfo getBean(PackageManager pm, PackageInfo pi) {

        if (pm == null || pi == null) return null;
        ApplicationInfo ai = pi.applicationInfo;
        String packageName = pi.packageName;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String packagePath = ai.sourceDir;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
        return new AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);
    }

    /**
     * This mehtod return list of AppInfo objects, with applications info
     * @param context: Application context
     */
    public static List<AppInfo> getAppsInfo(Context context) {

        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            AppInfo ai = getBean(pm, pi);
            if (ai == null) continue;
            list.add(ai);
        }
        return list;
    }

    /**
     * This method clean app dirs
     * @param context: Application context
     * @param dirPaths: Application directories (plural)
     */
    public static boolean cleanAppData(Context context, String... dirPaths) {

        File[] dirs = new File[dirPaths.length];
        int i = 0;
        for (String dirPath : dirPaths) {
            dirs[i++] = new File(dirPath);
        }
        return cleanAppData(dirs);
    }

    /**
     * This method clean app dirs
     * @param dirs: Application dirs (plural)
     */
    public static boolean cleanAppData(File... dirs) {

        boolean isSuccess = CleanUtils.cleanInternalCache();
        isSuccess &= CleanUtils.cleanInternalDbs();
        isSuccess &= CleanUtils.cleanInternalSP();
        isSuccess &= CleanUtils.cleanInternalFiles();
        isSuccess &= CleanUtils.cleanExternalCache();
        for (File dir : dirs) {
            isSuccess &= CleanUtils.cleanCustomCache(dir);
        }
        return isSuccess;
    }
}