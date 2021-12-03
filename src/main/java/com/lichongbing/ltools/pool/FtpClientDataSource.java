package com.lichongbing.ltools.pool;


import com.lichongbing.ltools.proxy.CglibProxy;
import com.lichongbing.ltools.proxy.IFtpHandler;
import com.lichongbing.ltools.proxy.IFtpHandlerImpl;

/**
 * ftp 连接池，解决每次都需要用去连接池
 */
public class FtpClientDataSource {


    private FtpClientPool ftpClientPool;

    public FtpClientDataSource(FtpClientPool ftpClientPool){
        this.ftpClientPool=ftpClientPool;
    }

    /**
     * 获取ftp操作对象
     * @return
     */
    public IFtpHandler getFtpClientHandler(){
        CglibProxy cglib = new CglibProxy();//实例化CglibProxy对象
        IFtpHandler iFtpHandler =  (IFtpHandler) cglib.getCglibProxy(new IFtpHandlerImpl());//获取代理对象
        return iFtpHandler;
    }
}
