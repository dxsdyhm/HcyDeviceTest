package com.rockchip.deviceregist.Entry;

/**
 * @Author: dxs
 * @time: 2020/5/6
 * @Email: duanxuesong12@126.com
 */
public class SendInfo {
    private String wifiMacAddr="";
    private String macAddr="";
    private String bluetoothAddr="";
    private String cpuserial="";
    private String fingerprint="";
    private String licenseCode="";
    private String model="";

    public SendInfo(String wifiMacAddr, String macAddr, String bluetoothAddr, String cpuserial, String fingerprint, String licenseCode, String model) {
        this.wifiMacAddr = wifiMacAddr;
        this.macAddr = macAddr;
        this.bluetoothAddr = bluetoothAddr;
        this.cpuserial = cpuserial;
        this.fingerprint = fingerprint;
        this.licenseCode = licenseCode;
        this.model = model;
    }

    public SendInfo() {
    }


    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
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

    public String getCpuserial() {
        return cpuserial;
    }

    public void setCpuserial(String cpuserial) {
        this.cpuserial = cpuserial;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "SendInfo{" +
                "wifiMacAddr='" + wifiMacAddr + '\'' +
                ", macAddr='" + macAddr + '\'' +
                ", bluetoothAddr='" + bluetoothAddr + '\'' +
                ", cpuserial='" + cpuserial + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                ", licenseCode='" + licenseCode + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}
