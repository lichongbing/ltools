package com.lichongbing.ltools.utils;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 3:17 下午
 * @description: TODO
 */
public class SM4_Context {
    public int mode;

    public long[] sk;

    public boolean isPadding;

    public SM4_Context()
    {
        this.mode = 1;
        this.isPadding = true;
        this.sk = new long[32];
    }
}

