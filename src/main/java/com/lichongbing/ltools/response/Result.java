package com.lichongbing.ltools.response;

import lombok.Data;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 11:48 上午
 * @description: TODO
 */
@Data
public class Result {

    private String id;
    private String iid;  ///业务号，用于区别哪个业务返回的结果
    private String name;  //业务名称
    private String resultCode;  //返回结果码值
    private String resultMessage;  //返回结果 （成功、失败）
    private String createDate;   //保存时间
    private Integer dataCount;  //返回数据条数
    private String resultData;  //返回数据
    private String xzqdm;  //行政区代码
    private String xzqmc;  //行政区名称
    private String status;  //业务办理状态
    /**
     *@author lichongbing
     *@update 2021/10/22 2:10 下午
     *@description: 增加字段
     */
    private String czlx; // 操作接口类型: (0: 受理消息接受， 1：契税完税信息， 2：契税完税信息pdf)
    private String czxz;// 操作性质 (0:推送数据 1:查询进度)
    private String tslx;//操作性质 (0:推送数据 1:查询进度)

}

