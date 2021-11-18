package com.rockchip.devicetest.testcase.impl;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.blankj.utilcode.util.Utils;
import com.rockchip.devicetest.constants.ParamConstants;
import com.rockchip.devicetest.model.TestCaseInfo;
import com.rockchip.devicetest.testcase.BaseTestCase;
import com.zhouyou.http.EasyHttp;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WifiSpTest extends BaseTestCase {
    private static String TAG = "WifiSpTest";
    private static String cmdIperfTest = "iperf -c 192.168.100.13 -i 1 -t 5 -w 1M";//-P 1
    private String speed = "15";

    public WifiSpTest(Context context, Handler handler, TestCaseInfo testcase) {
        super(context, handler, testcase);
    }

    @Override
    public void onTestInit() {

    }

    @Override
    public boolean onTesting() {
        //iperf测试
        //读取配置信息
        //1.执行命令由配置文件提供
        String cmdIperf = cmdIperfTest;
        if (mTestCaseInfo != null && mTestCaseInfo.getAttachParams() != null) {
            //没有配置用默认
            Map<String, String> attachParams = mTestCaseInfo.getAttachParams();
            cmdIperf = attachParams.getOrDefault(ParamConstants.CMD_IPERF, cmdIperfTest);
            speed = attachParams.getOrDefault(ParamConstants.SPEED_MIN, "15");
        }
        LogUtils.e(cmdIperf);
        ShellUtils.execCmdAsync(cmdIperf, false, new Utils.Consumer<ShellUtils.CommandResult>() {
            @Override
            public void accept(ShellUtils.CommandResult commandResult) {
                LogUtils.e(commandResult);
                if (commandResult.result == 0) {
                    String[] Band = getThroughput(commandResult.successMsg);
                    try {
                        boolean test = ShowResult(Band, speed);
                        if (test) {
                            onTestSuccess(Band[0]);
                        } else {
                            onTestFail(Band[0]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onTestFail(Band[0]);
                    }
                } else {
                    if(TextUtils.isEmpty(commandResult.errorMsg)){
                        onTestFail("无");
                    }else {
                        onTestFail(commandResult.errorMsg);
                    }
                }
            }
        });
        return super.onTesting();
    }

    private String[] getThroughput(String str) {
        String regx = "0.0-.+?/sec";
        String regxD = "0.0- (\\S+) sec  (\\S+) MBytes  (\\S+) Mbits/sec";
        String result[] = {"", ""};
        Matcher matcher = Pattern.compile(regxD, Pattern.DOTALL | Pattern.MULTILINE).matcher(str);
        Log.i(TAG, "matcher regx : " + regxD + " is " + matcher.matches());
        while (matcher.find()) {
            result[0] = matcher.group()+" |"+speed;
            result[1] = matcher.group(3);
        }
        return result;
    }

    private boolean ShowResult(String[] result, String speed) throws Exception {
        float real = Float.parseFloat(result[1]);
        float limit = Float.parseFloat(speed);
        return real > limit;
    }
}
