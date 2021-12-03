package com.lichongbing.ltools.pool;

import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;

/**
 * 继承FTP基类
 */
public class FtpClient extends Ftp {

    public FtpClient(FtpConfig config, FtpMode mode) {
        super(config, mode);
    }
}
