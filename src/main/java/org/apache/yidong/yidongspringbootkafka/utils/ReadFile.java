package org.apache.yidong.yidongspringbootkafka.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.yidong.yidongspringbootkafka.bean.MetricsRawData1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Component
public class ReadFile {
    @Value("${dirTarget}")
    private String dirTarget;
    @Value("${dirSource}")
    private String dirSource;
    private static HashMap<String, MetricsRawData1> map = new HashMap<String, MetricsRawData1>();

    public void getMemory() {
        File file = new File(dirTarget, "memory.txt");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String ip = null;
                MetricsRawData1 metricsRawData1 = new MetricsRawData1(LocalDateTime.now().toString());
                String memory = null;
                if (line.contains("SUCCESS")) {
                    ip = line.split(" ")[0];
                    map.put(ip, metricsRawData1);
                    String s = bufferedReader.readLine();
                    memory = s.split(":")[1].split("%")[0];
                } else {
                    bufferedReader.readLine();
                    ip = line.split(" ")[0];
                    memory = "0";
                }
                map.put(ip, metricsRawData1);
                metricsRawData1.putMemory_utilization(memory);
                metricsRawData1.putIp(ip);
            }
        } catch (Exception e) {
            log.error("memory.txt", e);
        }
    }

    public void getCpu() {
        File file = new File(dirTarget, "cpu.txt");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String ip = null;
                String cpu = null;
                if (line.contains("SUCCESS")) {
                    ip = line.split(" ")[0];
                    cpu = bufferedReader.readLine();
                } else {
                    ip = line.split(" ")[0];
                    bufferedReader.readLine();
                    cpu = "0";
                }
                map.get(ip).putCpu_utilization(cpu);
            }

        } catch (Exception e) {
            log.error("cpu.txt", e);
        }
    }

    public void getPong() {
        File file = new File(dirTarget, "ping_results.log");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String ip = null;
                String ping = null;
                ip = line.split(" ")[1];
                ping = line.split(" ")[3].split("%")[0];
                map.get(ip).putValue_ping(ping);
            }

        } catch (Exception e) {
            log.error("pong.txt", e);
        }
    }

    public void getDisk() {
        File file = new File(dirTarget, "disk.txt");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String ip = null;
                String disk = null;
                if (line.contains("SUCCESS")) {
                    String s = bufferedReader.readLine();
                    ip = line.split(" ")[0];
                    disk = s.split("%")[0];
                } else {
                    bufferedReader.readLine();
                    ip = line.split(" ")[0];
                    disk = "0";
                }
                map.get(ip).putPeak_disk_space_utilization(disk);
            }
        } catch (Exception e) {
            log.error("disk.txt", e);
        }
    }

    public HashMap<String, MetricsRawData1> get() {
        File file = new File(dirTarget);
        File file1 = new File(dirSource);
        if(!file.exists()||!file1.exists()) {
            file.mkdirs();
            file1.mkdirs();
        }
        for(File file2 : file1.listFiles()) {
            file2.renameTo(new File(dirTarget+File.separator+file2.getName()));
            log.info("move file success {}",file2.getName());
        }
        getMemory();
        getCpu();
        getDisk();
        getPong();
        HashMap<String, MetricsRawData1> map1 = map;
        map=new HashMap<String, MetricsRawData1>();
        return map1;
    }
}
