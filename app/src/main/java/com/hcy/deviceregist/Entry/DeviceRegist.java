package com.hcy.deviceregist.Entry;

/**
 * @Author: dxs
 * @time: 2020/4/30
 * "data":{
 *         "surplus":"20",
 *         "publicIp":"220.231.152.23",
 *         "serialNO":"HCYRK331843209910700"
 *     }
 * @Email: duanxuesong12@126.com
 */
public class DeviceRegist extends BaseEntry<DeviceRegist> {
    public String surplus;
    public String publicIp;
    public String serialNO;

    @Override
    public String toString() {
        return "DeviceRegist{" +
                "surplus='" + surplus + '\'' +
                ", publicIp='" + publicIp + '\'' +
                ", serialNO='" + serialNO + '\'' +
                '}';
    }
}
