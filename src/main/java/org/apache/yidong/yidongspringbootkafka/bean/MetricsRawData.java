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
public class MetricsRawData {
    //todo 时间戳
    private String timestamp;
    //todo 追踪id
    private String traceId="swrzjzccpt";
    //todo 粒度
    private Integer granularity=86400;
    //todo 数据标签
    private Map<String, Object> dims = new HashMap<>();
    //todo 度量值
    private Map<String, Object> values = new HashMap<>();

    public MetricsRawData(String timestamp,double msisdn_null_4g,double msisdn_null_5g,double msisdn_null_homelog,double ip_null_homelog,double ip_null_idc) {
        this.timestamp = timestamp;
        dims.put("data_name","servicesystem_swrzjzccpt");
        //todo 业务系统英文缩写
        dims.put("object","swrzjzccpt");
        //todo 业务系统中文名 通用维保-日志存储平台
        dims.put("source","通用维保-日志存储平台");
        values.put("swrzjzccpt_4g_msisdn_null_rate",msisdn_null_4g);
        values.put("swrzjzccpt_5g_msisdn_null_rate",msisdn_null_5g);
        values.put("swrzjzccpt_homelog_msisdn_null_rate",msisdn_null_homelog);
        values.put("swrzjzccpt_homelog_ygwipdz_null_rate",ip_null_homelog);
        values.put("swrzjzccpt_idc_ygwipdz_null_rate",ip_null_idc);
    }
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
