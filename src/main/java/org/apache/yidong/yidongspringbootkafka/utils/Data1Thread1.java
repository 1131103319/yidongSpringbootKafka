package org.apache.yidong.yidongspringbootkafka.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.yidong.yidongspringbootkafka.bean.MetricsRawData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

@Slf4j
public class Data1Thread1 extends Thread {
    private String topic;
    private boolean isAsync;
    private final Producer<Integer, String> producer;
    private final Producer<Integer, String> producer1;
    private JDBCUtils jdbcUtils;
    public Data1Thread1(JDBCUtils jdbcUtils,String topic,Producer<Integer, String> producer, Producer<Integer, String> producer1) {
        this.topic = topic;
        this.producer = producer;
        this.producer1 = producer1;
        this.jdbcUtils = jdbcUtils;
    }

    public static String getStartTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(4).withMinute(0).withSecond(0).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = localDateTime.format(formatter);
        return format;
    }

    public static String getEndTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(3).withMinute(0).withSecond(0).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = localDateTime.format(formatter);
        return format;
    }

    @Override
    public void run() {
        log.info("New Producer: start.");
        int messageNo = 1;
        try {
            String startTime = getStartTime();
            String endTime = getEndTime();
            double mdn4G = jdbcUtils.getmdn_4g(startTime, endTime);
            double mdn5G = jdbcUtils.getmdn_5g(startTime, endTime);
            double mdnhomelog = jdbcUtils.getmdn_homelog(startTime, endTime);
            double iphomelog = jdbcUtils.getip_homelog(startTime, endTime);
            double ipidc = jdbcUtils.getip_idc(startTime, endTime);
            //构造消息
            MetricsRawData metricsRawData = new MetricsRawData(LocalDateTime.now().toString(), mdn4G, mdn5G, mdnhomelog, iphomelog, ipidc);
            String messageStr = metricsRawData.toString();
            log.info("消息为：{}", metricsRawData);
            // 构造消息记录
            ProducerRecord<Integer, String> record = new ProducerRecord<Integer, String>(topic, 0, messageNo++, messageStr);
            try {
                // 同步发送
                RecordMetadata metadata = producer.send(record).get();
                log.info("消息发送成功，主题: {}, 分区: {}, 偏移量: {}", metadata.topic(), metadata.partition(), metadata.offset());
            } catch (InterruptedException ie) {
                log.error("发送消息时被中断: {producer}", ie);
            } catch (ExecutionException ee) {
                log.error("执行时出现异常: {producer}", ee);
            }

            try {
                // 同步发送
                RecordMetadata metadata = producer1.send(record).get();
                log.info("消息发送成功，主题: {}, 分区: {}, 偏移量: {}", metadata.topic(), metadata.partition(), metadata.offset());
            } catch (InterruptedException ie) {
                log.error("发送消息时被中断: {producer1}", ie);
            } catch (ExecutionException ee) {
                log.error("执行时出现异常: {producer1}", ee);
            }
            log.info("Producer: end.{}", LocalDateTime.now().toString());
        } catch (Exception e) {
            log.error("本次执行失败", e);
        }
    }
}