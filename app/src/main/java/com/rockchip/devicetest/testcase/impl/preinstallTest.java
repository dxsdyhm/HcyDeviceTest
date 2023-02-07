package com.rockchip.devicetest.testcase.impl;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.rockchip.devicetest.R;
import com.rockchip.devicetest.constants.ParamConstants;
import com.rockchip.devicetest.model.TestCaseInfo;
import com.rockchip.devicetest.model.TestResult;
import com.rockchip.devicetest.service.TestService;
import com.rockchip.devicetest.testcase.BaseTestCase;
import com.zhouyou.http.EasyHttp;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class preinstallTest extends BaseTestCase {
    private Context mContext;
    private String uPath = "";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            int index = msg.arg1;
            int AppsSize = msg.arg2;
            LogUtils.e(String.format("正在安装软件(%d/%d)", index, AppsSize));
            updateDetail(String.format("正在安装软件(%d/%d)", index, AppsSize));
            super.handleMessage(msg);
        }
    };

    public preinstallTest(Context context, Handler handler, TestCaseInfo testcase) {
        super(context, handler, testcase);
        mContext = context;
    }

    @Override
    public void onTestInit() {
        uPath = SPUtils.getInstance().getString(TestService.SP_KEY_ROOTPATH, "");
    }

    @Override
    public boolean onTesting() {
        if (mTestCaseInfo != null && mTestCaseInfo.getAttachParams() != null) {
            //没有配置用默认
            //Check specified wifi ap
            Map<String, String> attachParams = mTestCaseInfo.getAttachParams();
            String apkcounts = attachParams.getOrDefault(ParamConstants.APK_COUNTS, "0");
            String apkpath = attachParams.getOrDefault(ParamConstants.APK_PATH, "apps");
            int count = Integer.parseInt(apkcounts);
            startInstall(apkpath, count);
            ThreadUtils.runOnUiThreadDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 300);
        }
        return super.onTesting();
    }

    private void startInstall(String apppath, int appcount) {
        //遍历指定目录安装软件
        List<File> applist = FileUtils.listFilesInDirWithFilter(uPath + File.separator + apppath, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".apk");
            }
        });
        int AppsSize = applist.size();
        if (AppsSize == 0 || AppsSize != appcount) {
            onTestFail(String.format("没有足够的文件(%d/%d)", AppsSize, appcount));
            return;
        }

        ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<InstallResult>() {
            @Override
            public InstallResult doInBackground() throws Throwable {
                int index = 0;
                int succes = 0;
                int error = 0;

                List<AppUtils.AppInfo> infos = new ArrayList<>();
                for (File file : applist) {
                    Message message = new Message();
                    message.what = 0;
                    message.arg1 = index + 1;
                    message.arg2 = AppsSize;
                    mHandler.sendMessage(message);
                    AppUtils.AppInfo info = AppUtils.getApkInfo(file);
                    LogUtils.e(file.getAbsolutePath());
                    ShellUtils.CommandResult result1 = ShellUtils.execCmd("pm install -r " + file.getAbsolutePath(), false);
                    if (result1.result == 0) {
                        succes++;
                    } else {
                        error++;
                        infos.add(info);
                    }
                    LogUtils.e(info.getPackageName() + "  --  " + result1);
                    index++;
                }
                return new InstallResult(succes, error, index);
            }

            @Override
            public void onSuccess(InstallResult result) {
                if (result.count != appcount) {
                    onTestFail(String.format("成功:%d,异常:%d,总计:%d", result.success, result.error, result.count));
                } else {
                    onTestSuccess(String.format("成功:%d,异常:%d,总计:%d", result.success, result.error, result.count));
                }
            }
        });
    }

    private class InstallResult {

        public InstallResult(int success, int error, int count) {
            this.success = success;
            this.error = error;
            this.count = count;
        }

        public int success;
        public int error;
        public int count;
    }
}
