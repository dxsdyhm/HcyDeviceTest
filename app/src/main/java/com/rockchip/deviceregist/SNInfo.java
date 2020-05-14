package com.rockchip.deviceregist;

/**
 * @Author: dxs
 * @time: 2020/5/13
 * ip:127.0.0.1
 *    port:33334
 *    协议：udp
 *
 *    {
 *     "cmd": 1,       // 1 获取，2，设置；
 *     "usid": "lxc",      // 序列号
 *     "mac": "00:0A:12:22:33:90",   // 有线mac
 *     "mac_bt": "00:0B:12:22:33:90",  // 蓝牙mac
 *     "mac_wifi": "00:0C:12:22:33:90",    // wifi mac
 *     "errString": "ok"     // 响应命令返回时带的错误码,
 *    }
 * @Email: duanxuesong12@126.com
 */
public class SNInfo{
    private int cmd;
    private String usid;
    private String mac;
    private String mac_bt;
    private String mac_wifi;
    private String errString;

    public SNInfo() {
    }

    public SNInfo(int cmd, String usid, String mac, String mac_bt, String mac_wifi, String errString) {
        this.cmd = cmd;
        this.usid = usid;
        this.mac = mac;
        this.mac_bt = mac_bt;
        this.mac_wifi = mac_wifi;
        this.errString = errString;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMac_bt() {
        return mac_bt;
    }

    public void setMac_bt(String mac_bt) {
        this.mac_bt = mac_bt;
    }

    public String getMac_wifi() {
        return mac_wifi;
    }

    public void setMac_wifi(String mac_wifi) {
        this.mac_wifi = mac_wifi;
    }

    public String getErrString() {
        return errString;
    }

    public void setErrString(String errString) {
        this.errString = errString;
    }

    @Override
    public String toString() {
        return "{" +
                "cmd=" + cmd +
                ", usid='" + usid + '\'' +
                ", mac='" + mac + '\'' +
                ", mac_bt='" + mac_bt + '\'' +
                ", mac_wifi='" + mac_wifi + '\'' +
                ", errString='" + errString + '\'' +
                '}';
    }
}
