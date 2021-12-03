package com.lichongbing.ltools.pool;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;

import com.lichongbing.ltools.config.FtpConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 工厂类
 */
@Slf4j
public class FTPClientFactory extends BasePooledObjectFactory<FtpClient> {


    private FtpConfiguration.FtpConfig properties;

    public FTPClientFactory(FtpConfiguration.FtpConfig properties){
        this.properties=properties;
    }

    //新建对象
    @Override
    public FtpClient create() throws Exception {
        FtpConfig ftpConfig=new FtpConfig();
        ftpConfig.setCharset(Charset.forName(properties.getCharset()));
        ftpConfig.setConnectionTimeout(properties.getConnectionTimeout());
        ftpConfig.setHost(properties.getIp());
        ftpConfig.setPort(properties.getPort());
        ftpConfig.setUser(properties.getUsername());
        ftpConfig.setPassword(properties.getPassword());
        ftpConfig.setSoTimeout(properties.getSoTimeout());
        FtpMode mode=null;
        if(ObjectUtil.isNull(properties.getModel())){
            mode=FtpMode.Active;
        }else{
            mode=properties.getModel().intValue()==0?FtpMode.Active:FtpMode.Passive;
        }
        FtpClient ftp=new FtpClient(ftpConfig,mode);
        System.out.println("创建一个FtpClient 连接");

        return ftp;
    }


    //包装的连接对象
    @Override
    public PooledObject<FtpClient> wrap(FtpClient ftp) {
        return new DefaultPooledObject<FtpClient>(ftp);
    }

    //销毁对象
    @Override
    public void destroyObject(PooledObject<FtpClient> p) throws Exception {
        //获取到销毁对象
       Ftp ftp=p.getObject();
       ftp.close();
        System.out.println("销毁对象FtpClient连接");
       super.destroyObject(p);
    }


    //验证对象
    @Override
    public boolean validateObject(PooledObject<FtpClient> p) {
        Ftp ftp=p.getObject();
        boolean connect = false;
        try {
            //发送心跳包
            connect = ftp.getClient().sendNoOp();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connect;
    }





}
