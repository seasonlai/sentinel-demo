package com.seewo.sentinel.common.init;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterParamFlowRuleManager;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cvte.psd.conf.core.ConfigService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author yangzaizhong
 * @description
 * @date 2019/3/11 下午4:24
 */
@Configuration
public class SentinelRuleInitService {


    private static final int REQUEST_TIME_OUT = 200;

    private static final String NAMESPACE_NAME = "setinelRule";
    private static final String FLOW_RULE_KEY = "flow.rules";
    private static final String PARAM_FLOW_RULE_KEY = "param.rules";
    private static final String DEGRADE_RULE_KEY = "degrade.rules";
    //默认规则
    private static final String DEFAULT_FLOW_RULES = "[]";

    @PostConstruct
    public void init() {
        //token client init
        initDynamicRuleProperty();
        initClientConfigProperty();
        //token server init
        initClusterFlowSupplier();
    }

    private void initDynamicRuleProperty() {

        String flowRules = ConfigService.getConfig(NAMESPACE_NAME).getProperty(FLOW_RULE_KEY, DEFAULT_FLOW_RULES);
        System.out.println("flowRules:"+flowRules);

        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ApolloDataSource<>(NAMESPACE_NAME,
                FLOW_RULE_KEY, DEFAULT_FLOW_RULES,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                }));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleDataSource = new ApolloDataSource<>(NAMESPACE_NAME,
                PARAM_FLOW_RULE_KEY, DEFAULT_FLOW_RULES,
                source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
                }));
        ParamFlowRuleManager.register2Property(paramFlowRuleDataSource.getProperty());

        ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new ApolloDataSource<>(NAMESPACE_NAME,
                DEGRADE_RULE_KEY, DEFAULT_FLOW_RULES,
                source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
                }));
        DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
    }

    private void initClientConfigProperty() {
        ClusterClientConfig clientConfig = new ClusterClientConfig();
        clientConfig.setRequestTimeout(REQUEST_TIME_OUT);
        ClusterClientConfigManager.applyNewConfig(clientConfig);
    }

    /**
     * 初始化集群限流的Supplier
     * 这样如果后期集群限流的规则发生变更的话，系统可以自动感知到
     */
    private void initClusterFlowSupplier() {
        // 为集群流控注册一个Supplier，该Supplier会根据namespace动态创建数据源
        ClusterFlowRuleManager.setPropertySupplier(namespace -> {
            ReadableDataSource<String, List<FlowRule>> ds = new ApolloDataSource<>(NAMESPACE_NAME, FLOW_RULE_KEY,
                    DEFAULT_FLOW_RULES, source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
            }));
            return ds.getProperty();
        });

        ClusterParamFlowRuleManager.setPropertySupplier(namespace -> {
            ReadableDataSource<String, List<ParamFlowRule>> ds = new ApolloDataSource<>(NAMESPACE_NAME,
                    PARAM_FLOW_RULE_KEY, DEFAULT_FLOW_RULES,
                    source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
                    }));
            return ds.getProperty();
        });
    }
}
