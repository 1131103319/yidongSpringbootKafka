package org.apache.yidong.yidongspringbootkafka.utils;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer{
    private static final Logger LOG = LoggerFactory.getLogger(Producer.class);
    private static volatile KafkaProducer<Integer, String> producer=null;
    private static final Properties props = new Properties();
    // Broker地址列表
    private static final String bootstrapServers = "bootstrap.servers";
    // 客户端ID
    private static final String clientId = "client.id";
    // Key序列化类
    private static final String keySerializer = "key.serializer";
    // Value序列化类
    private static final String valueSerializer = "value.serializer";
    //默认发送20条消息
    private static final int messageNumToSend = 100;


    /*** 新Producer 构造函数
     */
    public static void setProducer(String clientid,String bootstrapservers) {
        // Key序列化类
        props.put(keySerializer,
                "org.apache.kafka.common.serialization.IntegerSerializer");
        // Value序列化类
        props.put(valueSerializer,
                "org.apache.kafka.common.serialization.StringSerializer");
        // 客户端ID
        props.put(clientId,clientid);
        // Broker地址列表
        props.put(bootstrapServers,bootstrapservers);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 32768);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 100);
        props.put(ProducerConfig.ACKS_CONFIG,"all");
        props.put(ProducerConfig.RETRIES_CONFIG, "10");
        producer = new KafkaProducer<Integer, String>(props);
    }
    public static org.apache.kafka.clients.producer.Producer<Integer, String> getProducer(String clientId,String bootstrapservers) {
        if(producer == null){
            synchronized (Producer.class){
                if(producer == null){
                    setProducer(clientId,bootstrapservers);
                }
            }
        }
        return producer;
    }
}