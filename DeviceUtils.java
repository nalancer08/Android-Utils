public class DeviceUtils {

    private DeviceUtils() {}

    /**
     * This method return a boolean value if the device is root or not
     */
    public static boolean isDeviceRooted() {

        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
                "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * This version get the SDK Version
     */
    public static int getSDKVersion() {

        return android.os.Build.VERSION.SDK_INT;
    }


    /**
     * This method the Android ID
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID() {
        
        return Settings.Secure.getString(Utils.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * This method get and return Mac Addres, only works with a connection internet
     * @code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     * @code <uses-permission android:name="android.permission.INTERNET"/>
     */
    public static String getMacAddress() {

        String macAddress = getMacAddressByWifiInfo();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByNetworkInterface();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByFile();
        if (!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        }
        return "please open wifi";
    }

    /**
     * This method its actually the manager of wifi, to get mac addres to be process
     * @code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     */
    @SuppressLint("HardwareIds")
    private static String getMacAddressByWifiInfo() {

        try {
            WifiManager wifi = (WifiManager) Utils.getContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                WifiInfo info = wifi.getConnectionInfo();
                if (info != null) return info.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * This method get Wifi throw mobile network
     * @code <uses-permission android:name="android.permission.INTERNET"/>
     */
    private static String getMacAddressByNetworkInterface() {

        try {
            List<NetworkInterface> nis = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nis) {
                if (!ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02x:", b));
                    }
                    return res1.deleteCharAt(res1.length() - 1).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    /**
     * This method get mac addres to exec comand
     */
    private static String getMacAddressByFile() {

        ShellUtils.CommandResult result = ShellUtils.execCmd("getprop wifi.interface", false);
        if (result.result == 0) {
            String name = result.successMsg;
            if (name != null) {
                result = ShellUtils.execCmd("cat /sys/class/net/" + name + "/address", false);
                if (result.result == 0) {
                    if (result.successMsg != null) {
                        return result.successMsg;
                    }
                }
            }
        }
        return "02:00:00:00:00:00";
    }

    /**
     * This method get manufacter
     */

    public static String getManufacturer() {

        return Build.MANUFACTURER;
    }

    /**
     * This method get model device
     */
    public static String getModel() {

        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * This method shutdown the cellphone
     * @code <android:sharedUserId="android.uid.system"/>
     */
    public static void shutdown() {

        ShellUtils.execCmd("reboot -p", true);
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getContext().startActivity(intent);
    }

    /**
     * This method reboot the cellphone
     * @code <android:sharedUserId="android.uid.system"/>
     *
     */
    public static void reboot() {

        ShellUtils.execCmd("reboot", true);
        Intent intent = new Intent(Intent.ACTION_REBOOT);
        intent.putExtra("nowait", 1);
        intent.putExtra("interval", 1);
        intent.putExtra("window", 0);
        Utils.getContext().sendBroadcast(intent);
    }

    /**
     * This method reboot the celphone whit a reason
     * @code <android:sharedUserId="android.uid.system"/>
     * @param reason: Passed to the kernel to request a special boot mode, such as "recovery"
     */
    public static void reboot(String reason) {

        PowerManager mPowerManager = (PowerManager) Utils.getContext().getSystemService(Context.POWER_SERVICE);
        try {
            mPowerManager.reboot(reason);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reboot the celphone and send you to recovery
     */
    public static void rebootToRecovery() {
        ShellUtils.execCmd("reboot recovery", true);
    }

    /**
     * This method reboot the celphone and send you to bootloader
     */
    public static void rebootToBootloader() {

        ShellUtils.execCmd("reboot bootloader", true);
    }