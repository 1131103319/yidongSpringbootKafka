package org.apache.yidong.yidongspringbootkafka.bean;

import lombok.extern.slf4j.Slf4j;
import org.apache.yidong.yidongspringbootkafka.utils.GsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * MetricsRawData class
 * {'dims':{'data_name':'cpu_men','ip':'10.20.1.10'},'values':{'cpu':'4','men':'10'}}
 *
 * @author jared
 * @date 2021/3/26 15:11
 */
@Slf4j
public class MetricsRawData1 {
    //todo 时间戳
    private String timestamp;
    //todo 追踪id
    private String traceId="swrzjzccpt_yj";
    //todo 粒度
    private Integer granularity=300;
    //todo 数据标签
    private Map<String, Object> dims = new HashMap<>();
    //todo 度量值
    private Map<String, Object> values = new HashMap<>();

    public MetricsRawData1(String timestamp) {
        this.timestamp = timestamp;
        dims.put("data_name","servicesystem_swrzjzccpt_yj");
        //todo 业务系统英文缩写
        dims.put("object","swrzjzccpt_yj");
        //todo 业务系统中文名 通用维保-日志存储平台
        dims.put("source","上网日志集中存储平台");
    }
    public void putIp(String ip) {
        dims.put("ip",ip);
    }
    public void putCpu_utilization(String cpu_utilization) {
        values.put("cpu_utilization",cpu_utilization);
    }
    public void putMemory_utilization(String memory_utilization) {
        values.put("memory_utilization",memory_utilization);
    }
    public void putValue_ping(String value_ping) {
        values.put("value_ping",value_ping);
    }
    public void putPeak_disk_space_utilization(String peak_disk_space_utilization) {
        values.put("peak_disk_space_utilization",peak_disk_space_utilization);
    }
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
