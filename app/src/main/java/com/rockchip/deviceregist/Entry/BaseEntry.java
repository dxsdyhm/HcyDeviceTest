package com.rockchip.deviceregist.Entry;

import com.zhouyou.http.model.ApiResult;

import java.io.Serializable;

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
public class BaseEntry<T> extends ApiResult<T> implements Serializable {

}
