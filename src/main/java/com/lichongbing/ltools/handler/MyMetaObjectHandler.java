package com.lichongbing.ltools.handler;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 11:47 上午
 * @description: TODO
 */

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动填充
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);  //创建时间
        this.setFieldValByName("updateTime", new Date(), metaObject); //修改时间
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject); //修改时间
    }
}

