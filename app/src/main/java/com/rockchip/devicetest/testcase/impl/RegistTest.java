package com.rockchip.devicetest.testcase.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.rockchip.deviceregist.Entry.DeviceRegist;
import com.rockchip.deviceregist.Entry.SendInfo;
import com.rockchip.deviceregist.SNInfo;
import com.rockchip.deviceregist.UDPClient;
import com.rockchip.devicetest.ConfigFinder;
import com.rockchip.devicetest.IndexActivity;
import com.rockchip.devicetest.model.TestCaseInfo;
import com.rockchip.devicetest.model.TestResult;
import com.rockchip.devicetest.service.TestService;
import com.rockchip.devicetest.testcase.BaseTestCase;
import com.rockchip.devicetest.utils.SystemInfoUtils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @Author: dxs
 * @time: 2020/5/6
 * 只有当Wifi或者Lan至少一个测试通过，才开启联网测试
 * @Email: duanxuesong12@126.com
 */
public class RegistTest extends BaseTestCase {
    private int UDPERROR_CLIENT_NULL=-1;
    private int UDPERROR_CLIENT_ERROR=-2;
    private Context mContext;
    private SendInfo sendInfo=null;
    private DeviceRegist deviceRegist;
    private String path;
    private static final int DEFAULT_INTERNET_TIMEOUT = 14 * 1000;
    public RegistTest(Context context, Handler handler, TestCaseInfo testcase) {
        super(context, handler, testcase);
        mContext = context;
    }

    @Override
    public void onTestInit() {
        //config
        path=ConfigFinder.logfilePath+File.separator+"log"+File.separator+ TimeUtils.date2String(new Date(),"yyyyMMdd")+".txt";
        init();
    }

    @Override
    public boolean onTesting() {
        if (TextUtils.isEmpty(sendInfo.getLicenseCode())) {
            onTestFail("未发现授权码");
            return false;
        }
        EasyHttp.post("api/v1/device/active")
                .upJson(GsonUtils.toJson(sendInfo))
                .execute(new SimpleCallBack<DeviceRegist>() {
                    @Override
                    public void onError(ApiException e) {
                        onTestFail("error:" + e.getCode());
                    }

                    @Override
                    public void onSuccess(DeviceRegist device) {
                        deviceRegist=device;

                        sendInfo(deviceRegist);
                    }
                });
        setTestTimeout(DEFAULT_INTERNET_TIMEOUT);
        return super.onTesting();
    }

    @Override
    public boolean onTestHandled(TestResult result) {
        disconnect();
        return super.onTestHandled(result);
    }

    private void init() {
        sendInfo = new SendInfo();
        sendInfo.setWifiMacAddr(SystemInfoUtils.getWifiMac(mContext));
        sendInfo.setMacAddr(SystemInfoUtils.getMac(mContext));
        sendInfo.setBluetoothAddr(IndexActivity.BlueMac);
        sendInfo.setFingerprint(SystemProperties.get("ro.build.fingerprint", ""));
        sendInfo.setCpuserial(SystemInfoUtils.getCpuSerial());
        String licencecode = null;
        try {
            List<String> licencecodes = FileIOUtils.readFile2List(ConfigFinder.findConfigFile(TestService.FILE_LICENCE, mContext));
            licencecode=licencecodes.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendInfo.setLicenseCode(licencecode);
        initManager();
    }
    UDPClient client=null;
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String result= (String) msg.obj;
                    SNInfo snInfo=GsonUtils.fromJson(result,SNInfo.class);
                    if("ok".equals(snInfo.getErrString())){
                        onTestSuccess("成功:" + deviceRegist.getSerialNO() + " 剩余：" + deviceRegist.getSurplus());
                        writeLog(deviceRegist);
                    }else {
                        onTestFail("error:"+UDPERROR_CLIENT_ERROR);
                    }
                    break;
                default:
                    onTestFail("error:"+msg.what);
                    break;
            }
        }
    };
    private void initManager() {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        client = new UDPClient(handler);
        exec.execute(client);
    }

    public void sendInfo(DeviceRegist deviceRegist){
        if(client!=null){
            final JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("cmd",2);
                if(!TextUtils.isEmpty(deviceRegist.getSerialNO())){
                    jsonObject.put("usid",deviceRegist.getSerialNO());
                }
                if(!TextUtils.isEmpty(deviceRegist.getMacAddr())){
                    jsonObject.put("mac",deviceRegist.getMacAddr());
                }
                if(!TextUtils.isEmpty(deviceRegist.getBluetoothAddr())){
                    jsonObject.put("mac_bt",deviceRegist.getBluetoothAddr());
                }
                if(!TextUtils.isEmpty(deviceRegist.getWifiMacAddr())){
                    jsonObject.put("mac_wifi",deviceRegist.getWifiMacAddr());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.send(jsonObject.toString());
                }
            }).start();
        }else {
            onTestFail("error:"+UDPERROR_CLIENT_NULL);
        }
    }

    private void disconnect(){
        if(client!=null){
            client.setUdpLife(false);
        }
    }

    private void writeLog(DeviceRegist content){
        String co=content.getSerialNO()+"|"+content.getPublicIp()+"|"+System.currentTimeMillis()+"|"+content.getSurplus()+"\n";
        FileIOUtils.writeFileFromString(path,co,true);
    }
}
