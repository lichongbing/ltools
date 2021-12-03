package com.lichongbing.ltools.pool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 连接池管理对象
 */
@Slf4j
public class FtpClientPool extends GenericObjectPool<FtpClient> {


    public FtpClientPool(PooledObjectFactory factory, GenericObjectPoolConfig genericObjectPoolConfig) {
        super(factory,genericObjectPoolConfig);
    }




}
