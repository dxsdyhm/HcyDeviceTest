package com.hcy.deviceregist;

import android.util.Log;

import com.hcy.deviceregist.Entry.DeviceRegist;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

/**
 * @Author: dxs
 * @time: 2020/4/30
 * @Email: duanxuesong12@126.com
 */
public class RegistByServer {
    /*
    {
    "wifiMacAddr":"e0:76:d0:38:69:10",
    "macAddr":"e0:76:d0:38:69:11",
    "bluetoothAddr":"e0:76:d0:38:69:12",
    "licenseCode":"E2BC1418F1394b1396A546CE9483AB4ECC84DCF549C843dcBCAE6B456E9B52A7"
    }
     */
    public static void HttpTest() {
        EasyHttp.post("/active")
                .params("wifiMacAddr","")
                .params("macAddr","")
                .params("bluetoothAddr","")
                .params("licenseCode","")
                .execute(new SimpleCallBack<DeviceRegist>() {
                    @Override
                    public void onError(ApiException e) {
                        Log.e("dxsTest","e:"+e);
                    }

                    @Override
                    public void onSuccess(DeviceRegist deviceRegist) {
                        Log.e("dxsTest","deviceRegist:"+deviceRegist);
                    }
                });
    }
}
