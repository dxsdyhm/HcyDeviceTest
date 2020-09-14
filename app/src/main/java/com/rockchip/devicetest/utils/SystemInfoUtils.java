/*******************************************************************
 * Company:     Fuzhou Rockchip Electronics Co., Ltd
 * Description:
 * @author: fxw@rock-chips.com
 * Create at:   2014年5月8日 下午5:06:15
 *
 * Modification History:
 * Date         Author      Version     Description
 * ------------------------------------------------------------------
 * 2014年5月8日      fxw         1.0         create
 *******************************************************************/

package com.rockchip.devicetest.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;

public class SystemInfoUtils {

    private static final String FILENAME_PROC_VERSION = "/proc/version";
    private static final String FILENAME_MEMINFO = "/proc/meminfo";
    private static final String FILENAME_MAC = "/sys/class/net/eth0/address";
    private static final String FILENAME_WIFI_MAC = "/sys/class/net/wlan0/address";

    /**
     * Reads a line from the specified file.
     *
     * @param filename the file to read from
     * @return the first line, if any.
     * @throws IOException if the file couldn't be read
     */
    private static String readLine(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename), 256);
        try {
            return reader.readLine();
        } finally {
            reader.close();
        }
    }

    /**
     * 获取Mac地址
     */
    public static String getMac(Context context) {
        String mac = "";
        try {
            mac = readLine(FILENAME_MAC);
        } catch (Exception e) {
        }
        return mac;
    }

    /**
     * 获取Mac地址
     */
    public static String getWifiMac(Context context) {
        String mac = "";
        try {
            mac = readLine(FILENAME_WIFI_MAC);
        } catch (Exception e) {
        }
        return mac;
    }

    /**
     * 获取flash容量
     *
     * @param context
     * @return
     */
    public static String getFormattedFlashSpace(Context context) {
        String data = Environment.getDataDirectory().getPath();
        long totalspace = FileUtils.getFsTotalSize(data);

        long othersize = 0;
        String othersize_s = SystemProperties.get("ro.product.otherdata", "0");
        try {
            othersize = Long.parseLong(othersize_s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        totalspace += othersize;
        if (2147483648L < totalspace && totalspace < 4294967296L) {
            totalspace = 4000000000L;
        }
        if (4294967296L < totalspace && totalspace < 8589934592L) {
            totalspace = 8000000000L;
        }
        if(8589934592L < totalspace && totalspace < 17179869184L){
            totalspace = 16000000000L;
        }
        if(17179869184L < totalspace && totalspace < 34359738368L){
            totalspace = 32000000000L;
        }
        if(34359738368L < totalspace && totalspace < 68719476736L){
            totalspace = 64000000000L;
        }
        if(68719476736L < totalspace && totalspace < 137438953472L){
            totalspace = 128000000000L;
        }
        return Formatter.formatFileSize(context, totalspace);
    }

    /**
     * 获取内存容量
     */
    public static String getFormattedRamSpace(Context context) {
        try {
            String line = readLine(FILENAME_MEMINFO);
            int begin = line.indexOf(':');
            int end = line.indexOf('k');
            line = line.substring(begin + 1, end).trim();
            int total = Integer.parseInt(line);
            String space = Formatter.formatFileSize(context, total * 1024);
            if (total > 2200000)
                space = "4GB";
            if (total > 1200000 && total < 2200000)
                space = "2GB";
            if (total > 530000 && total < 1200000)
                space = "1GB";
            if (total > 270000 && total < 530000)
                space = "512MB";
            return space;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取App版本信息
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return pInfo.versionName;
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /**
     * 获取内核版本
     *
     * @return
     */
    public static String getFormattedKernelVersion() {
        try {
            return formatKernelVersion(readLine(FILENAME_PROC_VERSION));

        } catch (IOException e) {
            LogUtil.e(SystemInfoUtils.class, "IO Exception when getting kernel version for Device Info screen");
            return "Unavailable";
        }
    }

    public static String formatKernelVersion(String rawKernelVersion) {
        // Example (see tests for more):
        // Linux version 3.0.31-g6fb96c9 (android-build@xxx.xxx.xxx.xxx.com) \
        //     (gcc version 4.6.x-xxx 20120106 (prerelease) (GCC) ) #1 SMP PREEMPT \
        //     Thu Jun 28 11:02:39 PDT 2012

        final String PROC_VERSION_REGEX =
                "Linux version (\\S+) " + /* group 1: "3.0.31-g6fb96c9" */
                        "\\((\\S+?)\\) " +        /* group 2: "x@y.com" (kernel builder) */
                        "(?:\\(gcc.+? \\)) " +    /* ignore: GCC version information */
                        "(#\\d+) " +              /* group 3: "#1" */
                        "(?:.*?)?" +              /* ignore: optional SMP, PREEMPT, and any CONFIG_FLAGS */
                        "((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)"; /* group 4: "Thu Jun 28 11:02:39 PDT 2012" */

        Matcher m = Pattern.compile(PROC_VERSION_REGEX).matcher(rawKernelVersion);
        if (!m.matches()) {
            LogUtil.e(SystemInfoUtils.class, "Regex did not match on /proc/version: " + rawKernelVersion);
            return "Unavailable";
        } else if (m.groupCount() < 4) {
            LogUtil.e(SystemInfoUtils.class, "Regex match on /proc/version only returned " + m.groupCount()
                    + " groups");
            return "Unavailable";
        }
        return m.group(1) + "  " +                 // 3.0.31-g6fb96c9
                m.group(2) + " " + m.group(3) + "  " + // x@y.com #1
                m.group(4);                            // Thu Jun 28 11:02:39 PDT 2012
    }

    public static String getCpuSerial() {
        String cpuinfo = "";
        try {
            //读取CPU信息
            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            //查找CPU序列号
            for (int i = 1; i < 100; i++) {
                String str = input.readLine();
                if (str != null) {
                    //查找到序列号所在行
                    if (str.contains("Serial")) {
                        //提取序列号
                        cpuinfo = str.substring(str.indexOf(":") + 1, str.length());
                        //去空格
                        cpuinfo = cpuinfo.trim();
                        break;
                    }
                } else {
                    //文件结尾
                    break;
                }
            }
        } catch (Exception ex) {
            //赋予默认值
            ex.printStackTrace();
        }
        if(TextUtils.isEmpty(cpuinfo)){
            cpuinfo=getSoftWinnerCpuSerial();
        }
        return cpuinfo;
    }

    public static String getSoftWinnerCpuSerial() {
        String cpuinfo = "";
        try {
            //读取CPU信息
            Process pp = Runtime.getRuntime().exec("cat /sys/class/sunxi_info/sys_info");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            //查找CPU序列号
            for (int i = 1; i < 100; i++) {
                String str = input.readLine();
                if (str != null) {
                    //查找到序列号所在行
                    if (str.contains("sunxi_chipid")) {
                        //提取序列号
                        cpuinfo = str.substring(str.indexOf(":") + 1, str.length());
                        //去空格
                        cpuinfo = cpuinfo.trim();
                        break;
                    }
                } else {
                    //文件结尾
                    break;
                }
            }
        } catch (Exception ex) {
            //赋予默认值
            ex.printStackTrace();
        }
        return cpuinfo;
    }

    public static String getBlueMac() {
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (bluetooth != null) {
            String address = bluetooth.isEnabled() ? bluetooth.getAddress() : null;
            if (!TextUtils.isEmpty(address)) {
                return address.toLowerCase();
            } else {
                return "";
            }
        }
        return "";
    }

    public static String getVersion() {
        String version = "";
        String firm = SystemProperties.get("ro.product.firmware", "");
        version = SystemProperties.get("ro.product.version", "");
        return firm+"/"+version;
    }
}
