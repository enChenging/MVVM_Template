package com.release.base.utils.response;

import java.io.Serializable;


/**
 * Created by Circle on 2017/4/2 0002.
 */

public class CommonRes<T> implements Serializable {

    public int code;// 200
    public T info;
    public String message;


    public boolean success() {
        return code == 200;
    }

    public boolean isTokenExpire() {
        return code == 406;
    }
}
