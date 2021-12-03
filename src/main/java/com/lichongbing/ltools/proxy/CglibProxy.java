package com.lichongbing.ltools.proxy;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;

import com.lichongbing.ltools.pool.FtpClient;
import com.lichongbing.ltools.pool.FtpClientPool;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

//Cglib动态代理，实现MethodInterceptor接口
public class CglibProxy implements MethodInterceptor {

    private Object target;//需要代理的目标对象

    //重写拦截方法
    @Override
    public Object intercept(Object obj, Method method, Object[] arr, MethodProxy proxy) throws Throwable {
        FtpClientPool ftpClientPool = SpringUtil.getBean(FtpClientPool.class);
        FtpClient ftpClient = ftpClientPool.borrowObject();
       try{
           Method setFtpClient = ReflectUtil.getMethod(target.getClass(), "setFtpClient", FtpClient.class);
           setFtpClient.invoke(target,ftpClient);
           Object invoke = method.invoke(target, arr);//方法执行，参数：target 目标对象 arr参数数组
           return invoke;
       }catch (Exception e){
           throw  e;
       }finally {
           ftpClientPool.returnObject(ftpClient);
       }
    }


    //定义获取代理对象方法
    public Object getCglibProxy(Object objectTarget){
        //为目标对象target赋值
        this.target = objectTarget;
        Enhancer enhancer = new Enhancer();
        //设置父类,因为Cglib是针对指定的类生成一个子类，所以需要指定父类
        enhancer.setSuperclass(objectTarget.getClass());
        enhancer.setCallback(this);// 设置回调
        Object result = enhancer.create();//创建并返回代理对象
        return result;
    }


}
