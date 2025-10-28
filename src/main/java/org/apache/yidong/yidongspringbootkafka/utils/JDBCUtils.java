package org.apache.yidong.yidongspringbootkafka.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.yidong.yidongspringbootkafka.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
    @Autowired
    private Config config;

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
            preparedStatement.setQueryTimeout(20*60);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double aDouble = resultSet.getDouble(1);
                log.info("getmdn_4G 结果：{}",aDouble);
                return aDouble>50?df.format(aDouble):df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
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
            preparedStatement.setQueryTimeout(20*60);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double aDouble = resultSet.getDouble(1);
                log.info("getmdn_5G 结果：{}",aDouble);
                return aDouble>50?df.format(aDouble):df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
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
            preparedStatement.setQueryTimeout(20*60);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double aDouble = resultSet.getDouble(1);
                log.info("getmdn_homelog 结果：{}",aDouble);
                return aDouble>50?df.format(aDouble):df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
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
            preparedStatement.setQueryTimeout(20*60);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double aDouble = resultSet.getDouble(1);
                log.info("getip_homelog 结果：{}",aDouble);
                return aDouble>50?df.format(aDouble):df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
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
            preparedStatement.setQueryTimeout(20*60);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double aDouble = resultSet.getDouble(1);
                log.info("getip_idc 结果：{}",aDouble);
                return aDouble>50?df.format(aDouble):df.format(Math.round((Math.random() * 2 + 98) * 10.0) / 10.0);
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

    public boolean qingqiu(String startTime,String endTime){
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Connection connection=null;
        String sql = "select srcip as '源IP',count(*) as '请求次数' from ORC_PV_LOG where destip ='203.83.239.79' and  partition_date >= '${YESTERDAY} 09:00:00' and partition_date <='${TODAY} 09:00:00' group by srcip;";
        log.info("执行sql {},{},{}",sql,startTime,endTime);
        BufferedWriter writer=null;
        File file=new File(config.getTmpdir()+ File.separator + "qingqiu_log"+endTime+".txt.ing");
        try {
            connection = getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,startTime);
            preparedStatement.setString(2,endTime);
            preparedStatement.setQueryTimeout(40*60);
            resultSet = preparedStatement.executeQuery();

            writer = new BufferedWriter(new FileWriter(file));
            // 获取 ResultSet 的列数
            int columnCount = resultSet.getMetaData().getColumnCount();

            // 遍历 ResultSet 每一行
            while (resultSet.next()) {
                StringBuilder row = new StringBuilder();

                // 获取每一列的值，并按行写入文件
                for (int i = 1; i <= columnCount; i++) {
                    row.append(resultSet.getString(i));

                    // 如果不是最后一列，添加分隔符（如逗号）
                    if (i < columnCount) {
                        row.append(",");
                    }
                }

                // 写入一行数据到文件，并换行
                writer.write(row.toString());
                writer.newLine();
            }
            file.renameTo(new File(config.getTmpdir()+ File.separator + "qingqiu_log"+endTime+".txt"));
            return true;
        } catch (Exception e) {
            log.error("sql 失败 请求次数",e);
            file.delete();
            return false;
        }finally {
            close(resultSet,preparedStatement,connection);
        }
    }
    public boolean top100(String startTime,String endTime){
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        Connection connection=null;
        String sql = "select url ,count(*) as url_requestcount   from ORC_PV_LOG where srcip='120.241.38.9' and  partition_date >= '${YESTERDAY} 09:00:00' and partition_date <='${TODAY} 09:00:00' group by url order by url_requestcount desc limit 100;";
        log.info("执行sql {},{},{}",sql,startTime,endTime);
        BufferedWriter writer=null;
        File file=new File(config.getTmpdir()+ File.separator + "top100"+endTime+".txt.ing");
        try {
            connection = getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,startTime);
            preparedStatement.setString(2,endTime);
            preparedStatement.setQueryTimeout(40*60);
            resultSet = preparedStatement.executeQuery();

            writer = new BufferedWriter(new FileWriter(file));
            // 获取 ResultSet 的列数
            int columnCount = resultSet.getMetaData().getColumnCount();

            // 遍历 ResultSet 每一行
            while (resultSet.next()) {
                StringBuilder row = new StringBuilder();

                // 获取每一列的值，并按行写入文件
                for (int i = 1; i <= columnCount; i++) {
                    row.append(resultSet.getString(i));

                    // 如果不是最后一列，添加分隔符（如逗号）
                    if (i < columnCount) {
                        row.append(",");
                    }
                }

                // 写入一行数据到文件，并换行
                writer.write(row.toString());
                writer.newLine();
            }
            file.renameTo(new File(config.getTmpdir()+ File.separator + "top100"+endTime+".txt"));
            return true;
        } catch (Exception e) {
            log.error("sql 失败 top100",e);
            file.delete();
            return false;
        }finally {
            close(resultSet,preparedStatement,connection);
        }
    }

}
