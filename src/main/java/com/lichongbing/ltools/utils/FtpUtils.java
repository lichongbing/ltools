package com.lichongbing.ltools.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 3:06 下午
 * @description: TODO
 */
@Component
@Slf4j
public class FtpUtils {


    /**
     * 连接FTP服务器
     *
     * @param ip       FTP地址
     * @param port     端口
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public static FTPClient ftpConnect(String ip, String port, String userName, String password) {
        FTPClient client = new FTPClient();
        try {
            client.connect(ip, Integer.parseInt(port));
            client.login(userName, password);
            int replyCode = client.getReplyCode();  //是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                client.disconnect();
                log.error("----- ftp服务器连接失败----------");
                System.exit(1);
            }

            //设置主动模式， 防止在Linux上， 由于安全限制，可能某些端口没有开启，出现阻塞
            client.enterLocalPassiveMode();  //告诉对面服务器开一个端口
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);



        } catch (Exception e) {
            e.printStackTrace();
        }

        return client;
    }


    /**
     * 断开FTP服务器连接
     *
     * @param client
     * @throws IOException
     */
    public static void close(FTPClient client) throws IOException {
        if (client != null && client.isConnected()) {
            client.logout();
            client.disconnect();
        }
    }



}

