package com.hcy.deviceregist;

import android.content.Context;
import android.util.Log;

import com.hcy.deviceregist.Entry.DeviceRegist;
import com.hcy.devicetest.utils.SystemInfoUtils;
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
    public static void HttpTest(Context context) {
        Log.e("dxsTest","HttpTest:");
        EasyHttp.post("api/v1/device/active")
                .params("wifiMacAddr",SystemInfoUtils.getMac(context))
                .params("macAddr", SystemInfoUtils.getMac(context))
                .params("bluetoothAddr",SystemInfoUtils.getMac(context))
                .params("licenseCode","E2BC1418F1394b1396A546CE9483AB4ECC84DCF549C843dcBCAE6B456E9B52A7")
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
