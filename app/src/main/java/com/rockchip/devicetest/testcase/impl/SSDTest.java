package com.rockchip.devicetest.testcase.impl;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.rockchip.devicetest.constants.ParamConstants;
import com.rockchip.devicetest.model.TestCaseInfo;
import com.rockchip.devicetest.testcase.BaseTestCase;

import java.util.Map;

public class SSDTest extends BaseTestCase {
    private String dev="/dev/block/sda|/dev/block/sdb";
    public SSDTest(Context context, Handler handler, TestCaseInfo testcase) {
        super(context, handler, testcase);
    }

    @Override
    public boolean onTesting() {
        if (mTestCaseInfo != null && mTestCaseInfo.getAttachParams() != null) {
            //没有配置用默认
            Map<String, String> attachParams = mTestCaseInfo.getAttachParams();
            dev = attachParams.getOrDefault(ParamConstants.DEV_PATH, dev);
        }
        String[] des=dev.split("\\|");
        boolean allexist=true;
        for(String de:des){
            if(!FileUtils.isFileExists(de)){
                allexist=false;
                onTestFail(de);
                break;
            }
        }
        if(allexist){
            onTestSuccess(dev);
        }
        return super.onTesting();
    }
}
