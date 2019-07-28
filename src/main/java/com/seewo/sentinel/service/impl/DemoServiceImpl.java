package com.seewo.sentinel.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.seewo.sentinel.dto.DemoDto;
import com.seewo.sentinel.service.DemoService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class DemoServiceImpl implements DemoService {

    @Override
    @SentinelResource(value = "flowRuleTest", blockHandler = "flowRuleBlockHandler")
    public String flow(DemoDto demoDto) {
        return "hello " + demoDto.getName();
    }

    public String flowRuleBlockHandler(DemoDto dto, BlockException ex) {
        return String.format("<%s> blocked by Sentinel", dto.getName());
    }

    @Override
    public String freqParamFlow(Long uid) {
        Entry entry = null;
        String retVal;
        try {
            // 对参数 uid 的值进行限流
            entry = SphU.entry("paramRuleTest", EntryType.IN, 1, uid);
            retVal = uid + " passed";
        } catch (BlockException e) {
            retVal = uid + " blocked";
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
        return retVal;
    }

    @Override
    @SentinelResource(value = "degradeRuleTest", blockHandler = "circuitBreakFallBack")
    public String circuitBreak(DemoDto demoDto) {
        /**
         * 异常处理例子
         * 异常个数规则如下：
         * degrade.rules = [{"resource": "degradeRuleTest","count": 60,"limitApp": "default","grade": 2,"timeWindow": 60} ]
         * 异常比率规则如下：
         * degrade.rules = [{"resource": "degradeRuleTest","count": 0.5,"limitApp": "default","grade": 1,"timeWindow": 10} ]
         */
        int num = (new Random()).nextInt(30);
        if (num % 3 == 0) {
            System.out.println("+++++++++" + new Date());
            throw new RuntimeException("circuitBreak exception");
        } else {
            System.out.println(num + "---" + new Date());
        }

        /**
         * 响应时间例子,规则配置如下：
         * degrade.rules = [{"resource": "degradeRuleTest","count": 500,"limitApp": "default","grade": 0,"timeWindow": 10} ]
         */
        //        int time = (new Random()).nextInt(1000);
        //        System.out.println(time + "---" + new Date());
        //        try {
        //            Thread.sleep(time);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }

        return demoDto.getName();
    }

    public String circuitBreakFallBack(DemoDto demoDto,BlockException e) throws Exception {
        return "fallBack:" + demoDto.getName();
    }

}