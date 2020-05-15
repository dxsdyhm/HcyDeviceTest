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
    private String macAddr;
    private String wifiMacAddr;
    private String bluetoothAddr;

    public DeviceRegist() {
    }

    public DeviceRegist(String surplus, String publicIp, String serialNO, String macAddr, String wifiMacAddr, String bluetoothAddr) {
        this.surplus = surplus;
        this.publicIp = publicIp;
        this.serialNO = serialNO;
        this.macAddr = macAddr;
        this.wifiMacAddr = wifiMacAddr;
        this.bluetoothAddr = bluetoothAddr;
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

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getWifiMacAddr() {
        return wifiMacAddr;
    }

    public void setWifiMacAddr(String wifiMacAddr) {
        this.wifiMacAddr = wifiMacAddr;
    }

    public String getBluetoothAddr() {
        return bluetoothAddr;
    }

    public void setBluetoothAddr(String bluetoothAddr) {
        this.bluetoothAddr = bluetoothAddr;
    }

    @Override
    public String toString() {
        return "DeviceRegist{" +
                "surplus='" + surplus + '\'' +
                ", publicIp='" + publicIp + '\'' +
                ", serialNO='" + serialNO + '\'' +
                ", macAddr='" + macAddr + '\'' +
                ", wifiMacAddr='" + wifiMacAddr + '\'' +
                ", bluetoothAddr='" + bluetoothAddr + '\'' +
                '}';
    }
}
