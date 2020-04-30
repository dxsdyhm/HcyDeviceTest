package com.hcy.deviceregist.Entry;

/**
 * @Author: dxs
 * @time: 2020/4/30
 * {
 *     "code":"0",
 *     "msg":"成功",
 *     "data":{
 *         "surplus":"20",
 *         "publicIp":"220.231.152.23",
 *         "serialNO":"HCYRK331843209910700"
 *     }
 * }
 * @Email: duanxuesong12@126.com
 */
public class BaseEntry<T> {
    private String code;
    private String msg;
    private T data;

    public BaseEntry() {
    }

    public BaseEntry(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
