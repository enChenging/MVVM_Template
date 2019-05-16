package com.release.base.utils.response;

/**
 * Created by Circle on 2017/4/2 0002.
 */

public class BaseResponse<T> {
    public int code;// 200,
    public T data;
    public String msg;

    public boolean success() {
        return code == 200;
    }
    public boolean isTokenExpire() {
        return code == 406;
    }
}
