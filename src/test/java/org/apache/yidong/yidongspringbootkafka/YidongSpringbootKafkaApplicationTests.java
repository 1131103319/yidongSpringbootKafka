package org.apache.yidong.yidongspringbootkafka;

import com.jcraft.jsch.SftpException;
import org.apache.yidong.yidongspringbootkafka.utils.SftpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class YidongSpringbootKafkaApplicationTests {
    @Autowired
    private SftpUtil sftpUtil;

    @Test
    void contextLoads() throws SftpException {
        sftpUtil.login();
        Arrays.stream(sftpUtil.listFiles("/Users/wohaocai").toArray()).forEach(System.out::println);
        sftpUtil.logout();
    }
}