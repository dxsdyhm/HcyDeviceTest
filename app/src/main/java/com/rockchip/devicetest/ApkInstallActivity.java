package com.rockchip.devicetest;

import android.content.Intent;
import android.os.Bundle;
import android.providers.settings.GlobalSettingsProto;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.rockchip.devicetest.service.TestService;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApkInstallActivity extends BaseActivity{
    private String uPath="";
    private int AppsSize=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_install);
        uPath=getIntent().getStringExtra(TestService.EXTRA_KEY_UPATH);
        ThreadUtils.runOnUiThreadDelayed(new Runnable() {
            @Override
            public void run() {
                startInstall();
            }
        },300);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        uPath=intent.getStringExtra(TestService.EXTRA_KEY_UPATH);
        startInstall();
    }

    private void startInstall() {
        TextView result=findViewById(R.id.tx_result);
        TextView proces=findViewById(R.id.tx_prosse);
        //遍历指定目录安装软件
        LogUtils.e(uPath);
        List<File> applist=FileUtils.listFilesInDirWithFilter(uPath + File.separator + "apps", new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".apk");
            }
        });
        if(applist.size()==0){
            result.setText("没有符合要求的文件");
        }
        AppsSize=applist.size();
        int index=0;
        int succes=0;
        int error=0;
        List<AppUtils.AppInfo> infos=new ArrayList<>();
        for(File file:applist){
            proces.setText(String.format("正在安装软件(%d/%d)", index,AppsSize));
            AppUtils.AppInfo info=AppUtils.getApkInfo(file);
            LogUtils.e(file.getAbsolutePath());
            ShellUtils.CommandResult result1=ShellUtils.execCmd("pm install -r "+file.getAbsolutePath(),false);
            if(result1.result==0){
                succes++;
            }else {
                error++;
                infos.add(info);
            }
            LogUtils.e(info.getPackageName()+"  --  "+result1);
            index++;
        }
        //监听package增减
        //展示安装结果
        result.setText(String.format("安装完成(成功数量：%d,  异常数量：%d,  总计：%d)", succes,error,index));
        result.append("\n错误信息:");
        result.append(infos.toString());
    }
}
