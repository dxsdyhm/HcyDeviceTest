package com.hcy.devicetest.testcase.impl;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.Gson;
import com.hcy.deviceregist.Entry.BaseEntry;
import com.hcy.deviceregist.Entry.DeviceRegist;
import com.hcy.deviceregist.Entry.SendInfo;
import com.hcy.devicetest.IndexActivity;
import com.hcy.devicetest.model.TestCaseInfo;
import com.hcy.devicetest.testcase.BaseTestCase;
import com.hcy.devicetest.utils.SystemInfoUtils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.func.ApiResultFunc;
import com.zhouyou.http.model.ApiResult;

import java.net.NetworkInterface;

/**
 * @Author: dxs
 * @time: 2020/5/6
 * 只有当Wifi或者Lan至少一个测试通过，才开启联网测试
 * @Email: duanxuesong12@126.com
 */
public class RegistTest extends BaseTestCase {
    private Context mContext;
    private SendInfo sendInfo=null;
    public RegistTest(Context context, Handler handler, TestCaseInfo testcase) {
        super(context, handler, testcase);
        mContext=context;
    }

    @Override
    public void onTestInit() {
        //config
        sendInfo=new SendInfo();
        sendInfo.setWifiMacAddr(SystemInfoUtils.getWifiMac(mContext));
        sendInfo.setMacAddr(SystemInfoUtils.getMac(mContext));
        sendInfo.setBluetoothAddr(IndexActivity.BlueMac);
        sendInfo.setLicenseCode("E2BC1418F1394b1396A546CE9483AB4ECC84DCF549C843dcBCAE6B456E9B52A7");
        Log.e("dxs","sendinfo:"+sendInfo);
    }

    @Override
    public boolean onTesting() {
        EasyHttp.post("api/v1/device/active")
                .upJson(GsonUtils.toJson(sendInfo))
                .execute(new SimpleCallBack<DeviceRegist>() {
                    @Override
                    public void onError(ApiException e) {
                        onTestFail("error:"+e.getCode());
                    }

                    @Override
                    public void onSuccess(DeviceRegist deviceRegist) {
                        onTestSuccess("成功:"+deviceRegist.getSerialNO());
                    }
                });
        return super.onTesting();
    }
}
