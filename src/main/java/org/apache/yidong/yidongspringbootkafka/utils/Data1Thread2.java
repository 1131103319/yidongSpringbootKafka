package org.apache.yidong.yidongspringbootkafka.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.yidong.yidongspringbootkafka.bean.MetricsRawData1;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Slf4j
public class Data1Thread2 extends Thread {
    private String topic;
    private boolean isAsync;
    private final Producer<Integer, String> producer;
    private final Producer<Integer, String> producer1;
    private ReadFile readFile;

    public Data1Thread2(ReadFile readFile,String topic, Producer<Integer, String> producer, Producer<Integer, String> producer1) {
        this.topic = topic;
        this.producer = producer;
        this.producer1 = producer1;
        this.readFile=readFile;
    }

    @Override
    public void run() {
        log.info("New Producer: start.");
        int messageNo = 1;
        try {
            HashMap<String, MetricsRawData1> map = readFile.get();
            log.info("消息数为 {}", map.size());
            for (MetricsRawData1 data1 : map.values()) {
                // 构造消息记录
                String messageStr = data1.toString();
                log.info("消息为：{}", messageStr);
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
            }
            log.info("New Producer: end.{}", LocalDateTime.now().toString());
        } catch (Exception e) {
            log.error("本次发送异常", e);
        }
    }
}
