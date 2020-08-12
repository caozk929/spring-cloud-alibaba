package eking.spring.cloud.gateway.helper.sentinel;

import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.cloud.sentinel.datasource.config.NacosDataSourceProperties;
import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayParamFlowItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 *  Sentinel数据源配置
 * @author caozk  caozhaokui@kungeek.com
 * @version 1.00.00
 * @since
 */
@Service
public class SentinelDataSourceConfigure {

    @Autowired
    private SentinelProperties sentinelProperties;

    /**
     * 注册网关路由规则
     */
    @PostConstruct
    public void registerGateWayDataSource() {
        NacosDataSourceProperties nacosDsProp = sentinelProperties.getDatasource().get("ds-routes").getNacos();
        Properties configProperties = new Properties();
        configProperties.put("serverAddr", nacosDsProp.getServerAddr());
        configProperties.put("namespace", nacosDsProp.getNamespace());
        ReadableDataSource<String, Set<GatewayFlowRule>> flowRuleDataSource = new NacosDataSource<>(configProperties, nacosDsProp.getGroupId(), nacosDsProp.getDataId(),
                source -> JSON.parseObject(source, new TypeReference<Set<GatewayFlowRule>>() {
                }));
        GatewayRuleManager.register2Property(flowRuleDataSource.getProperty());
    }


    /**
     * 注册网关api规则
     */
    @PostConstruct
    public void registerApiRouteDataSource() {
        NacosDataSourceProperties nacosApiProp = sentinelProperties.getDatasource().get("ds-api").getNacos();
        Properties configProperties = new Properties();
        configProperties.put("serverAddr", nacosApiProp.getServerAddr());
        configProperties.put("namespace", nacosApiProp.getNamespace());
        ReadableDataSource<String, Set<ApiDefinition>> flowRuleDataSource = new NacosDataSource<>(configProperties, nacosApiProp.getGroupId(), nacosApiProp.getDataId(),
                source -> JSON.parseObject(source, new TypeReference<Set<ApiDefinition>>() {
                }));
        GatewayApiDefinitionManager.register2Property(flowRuleDataSource.getProperty());
    }
}
