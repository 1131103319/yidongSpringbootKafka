package org.apache.yidong.yidongspringbootkafka.utils;

import com.google.gson.Gson;
import org.apache.yidong.yidongspringbootkafka.bean.MetricsRawData;
import org.apache.yidong.yidongspringbootkafka.bean.MetricsRawData1;

public class GsonUtils {
    public static String toJson(MetricsRawData metricsRawData){
        Gson gson = new Gson();
        return gson.toJson(metricsRawData);
    }
    public static String toJson(MetricsRawData1 metricsRawData){
        Gson gson = new Gson();
        return gson.toJson(metricsRawData);
    }
}
