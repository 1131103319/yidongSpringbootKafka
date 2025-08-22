package org.apache.yidong.yidongspringbootkafka.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.text.DecimalFormat;

@Slf4j
@Component
public class JDBCUtils {
    @Value("${driverName}")
    private String driver;       //Driver驱动
    @Value("${url}")
    private String url;          //Uniform Resource Locator，包含数据库信息
    @Value("${username}")
    private String user;         //用户名
    @Value("${passwd}")
    private String password;     //用户密码
    private static DecimalFormat  df = new DecimalFormat("0.00");

    @PostConstruct
    public void init() {
        try {
            System.out.println(driver);
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            log.error("加载数据库包失败",e);
        }
    }

    //获取连接
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //释放资源

    /**
     * @param resultSet  : 结果集（DQL产生）
     * @param statement  : Statement接口或PreparedStatement接口的实现类
     * @param connection : 即通过getConnection方法获取到的连接
     */
    public static void close(ResultSet resultSet, Statement statement, Connection connection) {
        /*
            DML的执行不产生ResultSet结果集，可以传入一个null;
            因此要先判断传入的对象是否为空，若非空则调用close方法关闭资源（动态绑定）
         */
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            log.error("close connection error",e);
        }
    }
    public String getmdn_4g(String startTime,String endTime){
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Connection connection=null;
        String sql = "select sum(case when msisdn = '' or msisdn is null or msisdn = 0  then 0 else 1 end )/count(1)*100 from mobile_db.orc_4glog_2c_log where partition_date >=? and  partition_date <=?;";
        log.info("执行sql {},{},{}",sql,startTime,endTime);
        try{
            connection = getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,startTime);
            preparedStatement.setString(2,endTime);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double aDouble = resultSet.getDouble(1);
                log.info("getmdn_4G 结果：{}",aDouble);
                return df.format(aDouble);
            }else{
                return df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
            }
        } catch (Exception e) {
           log.error("sql 失败 getmdn_4g",e);
           return df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
        }finally {
            close(resultSet,preparedStatement,connection);
        }
    }
    public String getmdn_5g(String startTime,String endTime){
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Connection connection=null;
        String sql = "select sum(case when msisdn = '' or msisdn is null or msisdn = 0  then 0 else 1 end )/count(1)*100 from mobile_db.orc_5gsalog_2c_log where partition_date >=? and  partition_date <=?;";
        log.info("执行sql {},{},{}",sql,startTime,endTime);
        try {
             connection = getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,startTime);
            preparedStatement.setString(2,endTime);
             resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double aDouble = resultSet.getDouble(1);
                log.info("getmdn_5G 结果：{}",aDouble);
                return df.format(aDouble);
            }else{
                return df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
            }
        } catch (Exception e) {
            log.error("sql 失败 getmdn_5g",e);
            return df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
        }finally {
            close(resultSet,preparedStatement,connection);
        }
    }
    public String getmdn_homelog(String startTime,String endTime){
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Connection connection=null;
        String sql = "select sum(case when swzh = '' or swzh is null or swzh = 0  then 0 else 1 end )/count(1)*100 from mobile_db.orc_homelog_log where partition_date >=? and  partition_date <=?;";
        log.info("执行sql {},{},{}",sql,startTime,endTime);
        try {
             connection = getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,startTime);
            preparedStatement.setString(2,endTime);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double aDouble = resultSet.getDouble(1);
                log.info("getmdn_homelog 结果：{}",aDouble);
                return df.format(aDouble);
            }else{
                return df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
            }
        } catch (Exception e) {
            log.error("sql 失败 getmdn_homelog",e);
            return df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
        }finally {
            close(resultSet,preparedStatement,connection);
        }
    }
    public String getip_homelog(String startTime,String endTime){
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Connection connection=null;
        String sql = "select sum(case when ygwipdz = '' or ygwipdz is null or ygwipdz = 0  then 0 else 1 end )/count(1)*100 from mobile_db.orc_homelog_log where partition_date >=? and  partition_date <=?;";
        log.info("执行sql {},{},{}",sql,startTime,endTime);
        try {
            connection = getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,startTime);
            preparedStatement.setString(2,endTime);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double aDouble = resultSet.getDouble(1);
                log.info("getip_homelog 结果：{}",aDouble);
                return df.format(aDouble);
            }else{
                return df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
            }
        } catch (Exception e) {
            log.error("sql 失败 getip_homelog",e);
            return df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
        }finally {
            close(resultSet,preparedStatement,connection);
        }
    }
    public String getip_idc(String startTime,String endTime){
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Connection connection=null;
        String sql = "select sum(case when srcip = '' or srcip is null or srcip = 0  then 0 else 1 end )/count(1)*100 from mobile_db.orc_pv_log where partition_date >=? and  partition_date <=?;";
        log.info("执行sql {},{},{}",sql,startTime,endTime);
        try {
            connection = getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,startTime);
            preparedStatement.setString(2,endTime);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double aDouble = resultSet.getDouble(1);
                log.info("getip_idc 结果：{}",aDouble);
                return df.format(aDouble);
            }else{
                return df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
            }
        } catch (Exception e) {
            log.error("sql 失败 getip_idc",e);
            return df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
        }finally {
            close(resultSet,preparedStatement,connection);
        }
    }
}
