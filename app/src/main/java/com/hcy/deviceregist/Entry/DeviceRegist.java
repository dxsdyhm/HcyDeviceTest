package com.hcy.deviceregist.Entry;

import com.zhouyou.http.model.ApiResult;

import java.io.Serializable;

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
public class DeviceRegist implements Serializable {
    private String surplus;
    private String publicIp;
    private String serialNO;

    public DeviceRegist() {
    }

    public DeviceRegist(String surplus, String publicIp, String serialNO) {
        this.surplus = surplus;
        this.publicIp = publicIp;
        this.serialNO = serialNO;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public String getSerialNO() {
        return serialNO;
    }

    public void setSerialNO(String serialNO) {
        this.serialNO = serialNO;
    }

    @Override
    public String toString() {
        return "DeviceRegist{" +
                "surplus='" + surplus + '\'' +
                ", publicIp='" + publicIp + '\'' +
                ", serialNO='" + serialNO + '\'' +
                '}';
    }
}
