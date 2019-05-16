package com.release.base.utils.baserx;

/**
 * 服务器返回数据异常
 * Created by Circle on 2017/4/3 0003.
 */

public class ApiException extends Exception {
    public ApiException(String msg) {
        super(msg);
    }
}