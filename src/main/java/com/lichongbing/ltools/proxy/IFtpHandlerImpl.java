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

public class IFtpHandlerImpl implements IFtpHandler {


    private FtpClient ftpClient;

    @Override
    public void setFtpClient(FtpClient ftpClient) {
        this.ftpClient=ftpClient;
    }

    @Override
    public Ftp setBackToPwd(boolean backToPwd) {
        return ftpClient.setBackToPwd(backToPwd);
    }

    @Override
    public Ftp reconnectIfTimeout() {
        return ftpClient.reconnectIfTimeout();
    }

    @Override
    public boolean cd(String directory) {
        return ftpClient.cd(directory);
    }

    @Override
    public String pwd() {
        return ftpClient.pwd();
    }

    @Override
    public List<String> ls(String path) {
        return ftpClient.ls(path);
    }

    @Override
    public List<FTPFile> lsFiles(String path, Filter<FTPFile> filter) {
        return ftpClient.lsFiles(path,filter);
    }

    @Override
    public FTPFile[] lsFiles(String path) throws FtpException, IORuntimeException {
        return ftpClient.lsFiles(path);
    }

    @Override
    public boolean mkdir(String dir) throws IORuntimeException {
        return ftpClient.mkdir(dir);
    }

    @Override
    public int stat(String path) throws IORuntimeException {
        return ftpClient.stat(path);
    }

    @Override
    public boolean existFile(String path) throws IORuntimeException {
        return ftpClient.existFile(path);
    }

    @Override
    public boolean delFile(String path) throws IORuntimeException {
        return ftpClient.delFile(path);
    }

    @Override
    public boolean delDir(String dirPath) throws IORuntimeException {
        return ftpClient.delDir(dirPath);
    }

    @Override
    public boolean upload(String destPath, File file) {
        return ftpClient.upload(destPath,file);
    }

    @Override
    public boolean upload(String path, String fileName, File file) {
        return ftpClient.upload(path,fileName,file);
    }

    @Override
    public boolean upload(String path, String fileName, InputStream fileStream) {
        return ftpClient.upload(path,fileName,fileStream);
    }

    @Override
    public void download(String path, File outFile) {
        ftpClient.download(path,outFile);
    }

    @Override
    public void recursiveDownloadFolder(String sourcePath, File destDir) {
        ftpClient.recursiveDownloadFolder(sourcePath,destDir);
    }

    @Override
    public void download(String path, String fileName, File outFile) throws IORuntimeException {
        ftpClient.download(path,fileName,outFile);
    }

    @Override
    public void download(String path, String fileName, OutputStream out) {
        ftpClient.download(path,fileName,out);
    }

    @Override
    public void download(String path, String fileName, OutputStream out, Charset fileNameCharset) throws IORuntimeException {
        ftpClient.download(path,fileName,out,fileNameCharset);
    }

    @Override
    public FTPClient getClient() {
        return ftpClient.getClient();
    }

    @Override
    public boolean exist(String path) {
        return ftpClient.exist(path);
    }

    @Override
    public void mkDirs(String dir) {
        ftpClient.mkDirs(dir);
    }
}
