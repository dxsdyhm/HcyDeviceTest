package com.hcy.devicetest.testcase.impl;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.hcy.deviceregist.Entry.DeviceRegist;
import com.hcy.deviceregist.Entry.SendInfo;
import com.hcy.devicetest.ConfigFinder;
import com.hcy.devicetest.IndexActivity;
import com.hcy.devicetest.model.TestCaseInfo;
import com.hcy.devicetest.service.TestService;
import com.hcy.devicetest.testcase.BaseTestCase;
import com.hcy.devicetest.utils.SystemInfoUtils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;


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
        init();
    }

    @Override
    public boolean onTesting() {
        if(TextUtils.isEmpty(sendInfo.getLicenseCode())){
            onTestFail("未发现授权码");
            return false;
        }
        EasyHttp.post("api/v1/device/active")
                .upJson(GsonUtils.toJson(sendInfo))
                .execute(new SimpleCallBack<DeviceRegist>() {
                    @Override
                    public void onError(ApiException e) {
                        onTestFail("error:"+e.getCode());
                    }

                    @Override
                    public void onSuccess(DeviceRegist deviceRegist) {
                        onTestSuccess("成功:"+deviceRegist.getSerialNO()+"剩余："+deviceRegist.getSurplus());
                    }
                });
        return super.onTesting();
    }

    private void init(){
        sendInfo=new SendInfo();
        sendInfo.setWifiMacAddr(SystemInfoUtils.getWifiMac(mContext));
        sendInfo.setMacAddr(SystemInfoUtils.getMac(mContext));
        sendInfo.setBluetoothAddr(IndexActivity.BlueMac);
        sendInfo.setCpuserial(SystemInfoUtils.getCpuSerial());
        String licencecode= null;
        try {
            licencecode = FileIOUtils.readFile2String(ConfigFinder.findConfigFile(TestService.FILE_LICENCE,mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendInfo.setLicenseCode(licencecode);
    }
}
