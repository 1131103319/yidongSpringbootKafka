package org.apache.yidong.yidongspringbootkafka.utils;

import com.jcraft.jsch.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.yidong.yidongspringbootkafka.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Properties;
import java.util.Vector;
@Component
@Slf4j
@Data
public class SftpUtil {
    @Autowired
    private Config config;
    private ChannelSftp sftp;

    private Session session;
    /** FTP 登录用户名*/
    private String username;
    /** FTP 登录密码*/
    private String password;
    /** FTP 服务器地址IP地址*/
    private String host;
    /** FTP 端口*/
    private int port;
    private String dir;


    /**
     * 构造基于密码认证的sftp对象
     */
    public SftpUtil() {
    }

    @PostConstruct
    public void init() {
        this.username = config.getUsername();
        this.password = config.getPassword();
        this.host = config.getIp();
        this.port = config.getPort();
        log.info("SFTP配置初始化完成 - 主机: {}, 端口: {}, 用户名: {}", host, port, username);
    }

    /**
     * 连接sftp服务器
     *
     * @throws Exception
     */
    public void login(){
        try {
            JSch jsch = new JSch();
            log.info("sftp connect by host:{} username:{}",host,username);

            session = jsch.getSession(username, host, port);
            log.info("Session is build");
            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();
            log.info("Session is connected");

            Channel channel = session.openChannel("sftp");
            channel.connect();
            log.info("channel is connected");

            sftp = (ChannelSftp) channel;
            log.info(String.format("sftp server host:[%s] port:[%s] is connect successfull", host, port));
        } catch (JSchException e) {
            log.error("Cannot connect to specified sftp server : {}:{} \n Exception message is: {}", new Object[]{host, port, e.getMessage()});
        }
    }

    /**
     * 关闭连接 server
     */
    public void logout(){
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                log.info("sftp is closed already");
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
                log.info("sshSession is closed already");
            }
        }
    }

    /**
     * 将输入流的数据上传到sftp作为文件
     *
     * @param directory
     *            上传到该目录
     * @param sftpFileName
     *            sftp端文件名
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException{
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            log.warn("directory is not exist");
            sftp.mkdir(directory);
            sftp.cd(directory);
        }
        sftp.put(input, sftpFileName);
        log.info("file:{} is upload successful" , sftpFileName);
    }

    /**
     * 上传单个文件
     *
     * @param directory
     *            上传到sftp目录
     * @param uploadFile
     *            要上传的文件,包括路径
     * @throws FileNotFoundException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String uploadFile) throws FileNotFoundException, SftpException{
        File file = new File(uploadFile);
        upload(directory, file.getName(), new FileInputStream(file));
    }
    /**
     *批量上传
     *
     * @param remotePath
     * @param localPath
     * @return
     * @throws SftpException
     * @throws FileNotFoundException
     */
    public boolean bacthUploadFile(String remotePath, String localPath) throws FileNotFoundException, SftpException {
        int flag=0;
        File file = new File(localPath);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                upload(remotePath,files[i].getName(),new FileInputStream(files[i]));
                flag++;
            }
        }
        if (flag==files.length){
            return true;
        }
        return false;
    }
    /**
     * 检查文件是否存在
     * @param directory
     * @param sftpFileName
     * @return
     * @throws SftpException
     */
    public boolean checkFileExist(String directory, String sftpFileName) throws SftpException{
        if (directory== null ||"".equals(directory)) {
            return false;
        }
        Vector<?> fileList=listFiles(directory);
        String file=fileList.toString();
        if(file.contains(sftpFileName)) {
            return true;
        }
        return false;
    }
    /**
     * 下载文件
     *
     * @param directory
     *            下载目录
     * @param downloadFile
     *            下载的文件
     * @param saveFile
     *            存在本地的路径
     * @throws SftpException
     * @throws FileNotFoundException
     * @throws Exception
     */
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException{
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
        log.info("file:{} is download successful" , downloadFile);
    }
    /**
     * 下载文件
     * @param directory 下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     * @throws SftpException
     * @throws IOException
     * @throws Exception
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException{
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);
        byte[] fileData = IOUtils.toByteArray(is);

        log.info("file:{} is download successful" , downloadFile);
        return fileData;
    }
    /**
     * 获取文件输入流
     * @param filePath
     * @return
     * @throws SftpException
     * @throws IOException
     */
    public InputStream download(String filePath) throws SftpException, IOException{
        String directory=filePath.substring(filePath.indexOf("/", filePath.indexOf("/")+1),filePath.lastIndexOf("/"));
        String downloadFile=filePath.substring(filePath.lastIndexOf("/")+1,filePath.length());
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);
        return is;
    }
    /**
     * 删除文件
     *
     * @param directory
     *            要删除文件所在目录
     * @param deleteFile
     *            要删除的文件
     * @throws SftpException
     * @throws Exception
     */
    public void delete(String directory, String deleteFile) throws SftpException{
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }
    /**
     * 	删除本地文件
     *
     * @param filePath
     * @return
     */
    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()&&file.isFile()) {
            file.delete();
        }
    }
    /**
     * 列出目录下的文件
     *
     * @param directory
     *            要列出的目录
     * @return
     * @throws SftpException
     */
    public Vector<?> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }
}


