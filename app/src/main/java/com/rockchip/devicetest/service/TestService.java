/*******************************************************************
 * Company:     Fuzhou Rockchip Electronics Co., Ltd
 * Description:
 * @author: fxw@rock-chips.com
 * Create at:   2014年5月8日 下午9:45:08
 *
 * Modification History:
 * Date         Author      Version     Description
 * ------------------------------------------------------------------
 * 2014年5月8日      fxw         1.0         create
 *******************************************************************/

package com.rockchip.devicetest.service;


import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.rockchip.devicetest.AgingTestActivity;
import com.rockchip.devicetest.ApkInstallActivity;
import com.rockchip.devicetest.ConfigFinder;
import com.rockchip.devicetest.IndexActivity;
import com.rockchip.devicetest.TestApplication;
import com.rockchip.devicetest.testcase.UsbSettings;
import com.rockchip.devicetest.utils.LogUtil;
import com.rockchip.devicetest.utils.StorageUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

public class TestService extends Service {

    public static final String SP_CONFIG_FILE = "config";
    public static final String SP_KEY_FACTORY = "factory";
    public static final String EXTRA_KEY_TESTDATA = "TESTDATA";
    public static final String EXTRA_KEY_TESTFROM = "TESTFROM";
    public static final String EXTRA_KEY_UPATH = "UPATH";
    public static final String FILE_FACTORY_TEST = "Factory_Test.bin";
    public static final String FILE_AGING_TEST = "Aging_Test.bin";
    public static final String FILE_SN_TEST = "SN_Test.bin";
    public static final String FILE_LICENCE = "License.bin";
    public static final String FILE_INSTALLAPK = "install.bin";
    private TestApplication mApp;
    private boolean isStartingActivity;
    private boolean hasStartedActivity;
    private boolean hasCheckedMount;//检测Mount
    private boolean isShowingApp;//是否已显示当前应用
    private int mCheckMountCount;//检测次数
    private Handler mMainHandler;
    private StorageManager mStorageManager = null;
    private boolean mFirstMount;
    private String uPath = "";
    public static final String SP_KEY_ROOTPATH = "rootpath";

    //TODO FileObserve

    public void onCreate() {
        super.onCreate();
        mMainHandler = new Handler();
        mApp = (TestApplication) getApplication();
        mApp.mTestService = this;
        mFirstMount = false;
        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        ifilter.addDataScheme("file");
        registerReceiver(mReceiver, ifilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String startFrom = intent.getStringExtra(EXTRA_KEY_TESTFROM);
            if ("app".equals(startFrom)) {
                return super.onStartCommand(intent, flags, startId);
            } else if ("mount".equals(startFrom)) {
                uPath = intent.getStringExtra(EXTRA_KEY_UPATH);
                if (!mFirstMount) {
                    mFirstMount = true;
                    startTest();
                }
                LogUtil.d(this, "Rock Recv Mount action. " + SystemClock.uptimeMillis());
            } else if ("boot".equals(startFrom) || "system".equals(startFrom)) {
                LogUtil.d(this, "Rock Recv " + startFrom + " action. " + SystemClock.uptimeMillis());
                if (!hasCheckedMount && !hasStartedActivity && !isStartingActivity) {
                    hasCheckedMount = true;
                    SharedPreferences sp = getSharedPreferences(SP_CONFIG_FILE, Context.MODE_PRIVATE);
                    int checkCount = sp.getInt("check_mount", 0);
                    if (checkCount <= 8) {//只检测八次,避免影响用户使用
                        sp.edit().putInt("check_mount", ++checkCount).commit();
                        mMainHandler.post(mCheckStorageAction);
                    }
                    mFirstMount = true;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // 检查U盘/SDCard挂载情况
    private Runnable mCheckStorageAction = new Runnable() {
        public void run() {
            //	String udisk0 = Environment.getHostStorage_Extern_0_Directory().getAbsolutePath();
            //	String udisk1 = Environment.getHostStorage_Extern_1_Directory().getAbsolutePath();
            if (mStorageManager == null) {
                mStorageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
            }
            String sdcard_path = StorageUtils.getSDcardDir(mStorageManager);
            ;

            LogUtil.d(TestService.this, "Rock check storage " + mCheckMountCount);
            if (hasStartedActivity || isStartingActivity) {
                LogUtil.d(TestService.this, "Rock is starting activity. ");
                return;
            } else if (StorageUtils.isMountUSB(mStorageManager) || StorageUtils.isMountSD(mStorageManager)) {
                LogUtil.d(TestService.this, "Rock check storage, mounted. ");
                startTest();
            } else if (mCheckMountCount < 6) {
                mMainHandler.postDelayed(mCheckStorageAction, 3000);
            }
            mCheckMountCount++;
        }
    };

    // 判断是否已经Mount
    private boolean isMounted(String path) {
        if (mStorageManager == null) {
            mStorageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        }
        if (path == null)
            return false;
        String externalVolumeState = mStorageManager.getVolumeState(path);
        return externalVolumeState.equals(Environment.MEDIA_MOUNTED);
    }

    public void setShowingApp(boolean isShowing) {
        isShowingApp = isShowing;
        if (isShowingApp) {
            isStartingActivity = false;
            hasStartedActivity = true;
        }
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    //启动测试
    public void startTest() {
        mMainHandler.removeCallbacks(mDelayRunAction);
        mMainHandler.postDelayed(mDelayRunAction, 1500);
    }

    //执行测试
    private void handleTest() {
        //检测功能测试
        if (isInFactoryTest()) {
            LogUtil.d(this, "Rock do factory test. ");
            SPUtils.getInstance().put(SP_KEY_ROOTPATH, uPath);
            startActivityWait(IndexActivity.class);
        } else if (isInAgingTest()) {//检测老化测试
            startActivityWait(AgingTestActivity.class);
            LogUtil.d(this, "Rock do aging test.");
        } else if (isInSNTest()) {
            UsbSettings.enableADB(this);
            UsbSettings.setUsbSlaveMode();
        } else if (isInInstallApk()) {
            if (!TextUtils.isEmpty(uPath)) {
                LogUtil.d(this, "start install usb apk." + uPath);
                if(isShowInstall()){
                    startActivityWait(ApkInstallActivity.class);
                }else {
                    installApps(uPath);
                }
            } else {
                LogUtil.e(this, "install apk path is null");
            }
        } else {
            LogUtil.d(this, "It is not in factory/aging test mode.");
        }
    }

    private void startActivityWait(Class activity) {//可以改用广播来通知activity已创建
        if (isStartingActivity || isShowingApp || mApp.isOnTopApplication()) {
            return;
        }
        isStartingActivity = true;
        Intent agingIntent = new Intent();
        agingIntent.setClass(this, activity);
        agingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        agingIntent.putExtra(EXTRA_KEY_UPATH, uPath);
        startActivity(agingIntent);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mFirstMount) {//第一次执行完
                startTest();
            }
        }
    };

    private Runnable mDelayRunAction = new Runnable() {
        public void run() {
            LogUtil.d(this, "Rock Handle test. ");
            handleTest();
        }
    };

    /**
     * 检测是否有Factory_Test.bin文件
     */
    private boolean isInFactoryTest() {
        return ConfigFinder.hasConfigFile(FILE_FACTORY_TEST, this);
    }

    /**
     * 检测是否有Aging_Test.bin文件
     */
    private boolean isInAgingTest() {
        return ConfigFinder.hasConfigFile(FILE_AGING_TEST, this);
    }

    /**
     * 检测是否有SN_Test.bin文件
     */
    private boolean isInSNTest() {
        return ConfigFinder.hasConfigFile(FILE_SN_TEST, this);
    }

    /**
     * 检测是否有Install_apk.bin文件
     */
    private boolean isInInstallApk() {
        return ConfigFinder.hasConfigFile(FILE_INSTALLAPK, this);
    }

    private boolean isShowInstall(){
        List<String> list=FileIOUtils.readFile2List(FILE_INSTALLAPK);
        if(list!=null){
           return list.contains("showinstall:true");
        }
        return false;
    }

    private void installApps(String uPath){
        List<File> applist=FileUtils.listFilesInDirWithFilter(uPath + File.separator + "apps", new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".apk");
            }
        });
        for(File file:applist){
            AppUtils.AppInfo info=AppUtils.getApkInfo(file);
            ShellUtils.CommandResult result1=ShellUtils.execCmd("pm install -r "+file.getAbsolutePath(),false);
            LogUtils.e(info.getPackageName()+"  --  "+result1);
            if(result1.result!=0){
                ShellUtils.CommandResult result2=ShellUtils.execCmd("pm install -r "+file.getAbsolutePath(),true);
                LogUtils.e(info.getPackageName()+"  --  "+result2);
            }
        }
    }
}
