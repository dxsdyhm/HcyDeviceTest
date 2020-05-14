package com.rockchip.deviceregist.Entry;

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
    private String mac;
    private String macwifi;
    private String macbt;

    public DeviceRegist() {
    }

    public DeviceRegist(String surplus, String publicIp, String serialNO, String mac, String macwifi, String macbt) {
        this.surplus = surplus;
        this.publicIp = publicIp;
        this.serialNO = serialNO;
        this.mac = mac;
        this.macwifi = macwifi;
        this.macbt = macbt;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMacwifi() {
        return macwifi;
    }

    public void setMacwifi(String macwifi) {
        this.macwifi = macwifi;
    }

    public String getMacbt() {
        return macbt;
    }

    public void setMacbt(String macbt) {
        this.macbt = macbt;
    }

    @Override
    public String toString() {
        return "DeviceRegist{" +
                "surplus='" + surplus + '\'' +
                ", publicIp='" + publicIp + '\'' +
                ", serialNO='" + serialNO + '\'' +
                ", mac='" + mac + '\'' +
                ", macwifi='" + macwifi + '\'' +
                ", macbt='" + macbt + '\'' +
                '}';
    }
}
