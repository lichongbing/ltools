package com.lichongbing.ltools.proxy;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Filter;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpException;

import com.lichongbing.ltools.pool.FtpClient;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * ftp处理接口类
 */
public interface IFtpHandler {


    //初始化对象对自动调用此方法
    public void setFtpClient(FtpClient ftpClient);

    /**
     * 设置执行完操作是否返回当前目录
     *
     * @param backToPwd 执行完操作是否返回当前目录
     * @return this
     * @since 4.6.0
     */
    public Ftp setBackToPwd(boolean backToPwd);

    /**
     * 如果连接超时的话，重新进行连接 经测试，当连接超时时，client.isConnected()仍然返回ture，无法判断是否连接超时 因此，通过发送pwd命令的方式，检查连接是否超时
     *
     * @return this
     */
    public Ftp reconnectIfTimeout();

    /**
     * 改变目录
     *
     * @param directory 目录
     * @return 是否成功
     */
     public boolean cd(String directory);

    /**
     * 远程当前目录
     *
     * @return 远程当前目录
     * @since 4.1.14
     */
    public String pwd();

    public List<String> ls(String path) ;

    /**
     * 遍历某个目录下所有文件和目录，不会递归遍历<br>
     * 此方法自动过滤"."和".."两种目录
     *
     * @param path   目录
     * @param filter 过滤器，null表示不过滤，默认去掉"."和".."两种目录
     * @return 文件或目录列表
     * @since 5.3.5
     */
    public List<FTPFile> lsFiles(String path, Filter<FTPFile> filter);

    /**
     * 遍历某个目录下所有文件和目录，不会递归遍历
     *
     * @param path 目录，如果目录不存在，抛出异常
     * @return 文件或目录列表
     * @throws FtpException       路径不存在
     * @throws IORuntimeException IO异常
     */
    public FTPFile[] lsFiles(String path) throws FtpException, IORuntimeException;

    public boolean mkdir(String dir) throws IORuntimeException;

    /**
     * 获取服务端目录状态。
     *
     * @param path 路径
     * @return 状态int，服务端不同，返回不同
     * @since 5.4.3
     */
    public int stat(String path) throws IORuntimeException;

    /**
     * 判断ftp服务器文件是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     * @throws IORuntimeException IO异常
     */
    public boolean existFile(String path) throws IORuntimeException;

    public boolean delFile(String path) throws IORuntimeException;

    public boolean delDir(String dirPath) throws IORuntimeException ;

    /**
     * 上传文件到指定目录，可选：
     *
     * <pre>
     * 1. path为null或""上传到当前路径
     * 2. path为相对路径则相对于当前路径的子路径
     * 3. path为绝对路径则上传到此路径
     * </pre>
     *
     * @param destPath 服务端路径，可以为{@code null} 或者相对路径或绝对路径
     * @param file     文件
     * @return 是否上传成功
     */
    public boolean upload(String destPath, File file);

    /**
     * 上传文件到指定目录，可选：
     *
     * <pre>
     * 1. path为null或""上传到当前路径
     * 2. path为相对路径则相对于当前路径的子路径
     * 3. path为绝对路径则上传到此路径
     * </pre>
     *
     * @param file     文件
     * @param path     服务端路径，可以为{@code null} 或者相对路径或绝对路径
     * @param fileName 自定义在服务端保存的文件名
     * @return 是否上传成功
     * @throws IORuntimeException IO异常
     */
    public boolean upload(String path, String fileName, File file);

    /**
     * 上传文件到指定目录，可选：
     *
     * <pre>
     * 1. path为null或""上传到当前路径
     * 2. path为相对路径则相对于当前路径的子路径
     * 3. path为绝对路径则上传到此路径
     * </pre>
     *
     * @param path       服务端路径，可以为{@code null} 或者相对路径或绝对路径
     * @param fileName   文件名
     * @param fileStream 文件流
     * @return 是否上传成功
     * @throws IORuntimeException IO异常
     */
    public boolean upload(String path, String fileName, InputStream fileStream);

    /**
     * 下载文件
     *
     * @param path    文件路径
     * @param outFile 输出文件或目录
     */
    public void download(String path, File outFile);

    /**
     * 递归下载FTP服务器上文件到本地(文件目录和服务器同步)
     *
     * @param sourcePath ftp服务器目录
     * @param destDir    本地目录
     */
    public void recursiveDownloadFolder(String sourcePath, File destDir);

    /**
     * 下载文件
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @param outFile  输出文件或目录
     * @throws IORuntimeException IO异常
     */
    public void download(String path, String fileName, File outFile) throws IORuntimeException;

    /**
     * 下载文件到输出流
     *
     * @param path     文件路径
     * @param fileName 文件名
     * @param out      输出位置
     */
    public void download(String path, String fileName, OutputStream out);

    /**
     * 下载文件到输出流
     *
     * @param path            文件路径
     * @param fileName        文件名
     * @param out             输出位置
     * @param fileNameCharset 文件名编码
     * @throws IORuntimeException IO异常
     * @since 5.5.7
     */
    public void download(String path, String fileName, OutputStream out, Charset fileNameCharset) throws IORuntimeException;

    /**
     * 获取FTPClient客户端对象
     *
     * @return {@link FTPClient}
     */
    public FTPClient getClient();


    /**
     * 文件或目录是否存在
     *
     * @param path 目录
     * @return 是否存在
     */
    public boolean exist(String path);

    public void mkDirs(String dir);

}
