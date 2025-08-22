package org.apache.yidong.yidongspringbootkafka.test;

import static org.apache.yidong.yidongspringbootkafka.utils.Data1Thread1.getEndTime;
import static org.apache.yidong.yidongspringbootkafka.utils.Data1Thread1.getStartTime;

public class test {
    public static void main(String[] args) {
//        System.out.println(String.format("%.2f",Math.round((Math.random() * 2 + 98) * 10.0) / 10.0));
//        System.out.println(String.format("%.2f", 100.0));
//        DecimalFormat df = new DecimalFormat("0.00");
//        System.out.println(df.format(100.0));
        System.out.println(getStartTime(12));
        System.out.println(getEndTime(12));
    }
}
