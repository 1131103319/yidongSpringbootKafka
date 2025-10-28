package org.apache.yidong.yidongspringbootkafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sftp")
@Data
public class Config {
    public  String ip;
    public  Integer port;
    public  String username;
    public  String password;
    public  String rootpath;
    public String tmpdir;
}
