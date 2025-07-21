package org.apache.yidong.yidongspringbootkafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.yidong.yidongspringbootkafka.utils.Data1Thread1;
import org.apache.yidong.yidongspringbootkafka.utils.Data1Thread2;
import org.apache.yidong.yidongspringbootkafka.utils.JDBCUtils;
import org.apache.yidong.yidongspringbootkafka.utils.ReadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class service {
    @Value("${topic}")
    String topic;
    @Value("${bootstrap.servers1}")
    String bootstrapServer1;
    @Value("${bootstrap.servers2}")
    String bootstrapServer2;
    @Autowired
    JDBCUtils jdbcUtils;
    @Autowired
    ReadFile readFile;
    Producer<Integer, String> client1;
    Producer<Integer, String> client2;
    Data1Thread1 data1Thread1;
    Data1Thread2 data1Thread2;

    @PostConstruct
    public void init() {
        client1 = org.apache.yidong.yidongspringbootkafka.utils.Producer.getProducer("1", bootstrapServer1);
        client2 = org.apache.yidong.yidongspringbootkafka.utils.Producer.getProducer("2", bootstrapServer2);
        data1Thread1 = new Data1Thread1(jdbcUtils,topic, client1, client2);
        data1Thread2 = new Data1Thread2(readFile,topic, client1, client2);
    }
    @Scheduled(cron="${cron1}")
    public void cron1(){
        try {
            data1Thread1.run();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron="${cron2}")
    public void cron2(){
        try {
            data1Thread2.run();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

}
