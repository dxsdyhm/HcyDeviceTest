package com.hcy.deviceregist.Entry;

/**
 * @Author: dxs
 * @time: 2020/5/6
 * @Email: duanxuesong12@126.com
 */
public class SendInfo {
    private String wifiMacAddr="";
    private String macAddr="";
    private String bluetoothAddr="";
    private String licenseCode="";

    public SendInfo(String wifiMacAddr, String macAddr, String bluetoothAddr, String licenseCode) {
        this.wifiMacAddr = wifiMacAddr;
        this.macAddr = macAddr;
        this.bluetoothAddr = bluetoothAddr;
        this.licenseCode = licenseCode;
    }

    public SendInfo() {
    }

    public String getWifiMacAddr() {
        return wifiMacAddr;
    }

    public void setWifiMacAddr(String wifiMacAddr) {
        this.wifiMacAddr = wifiMacAddr;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getBluetoothAddr() {
        return bluetoothAddr;
    }

    public void setBluetoothAddr(String bluetoothAddr) {
        this.bluetoothAddr = bluetoothAddr;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    @Override
    public String toString() {
        return "SendInfo{" +
                "wifiMacAddr='" + wifiMacAddr + '\'' +
                ", macAddr='" + macAddr + '\'' +
                ", bluetoothAddr='" + bluetoothAddr + '\'' +
                ", licenseCode='" + licenseCode + '\'' +
                '}';
    }
}
