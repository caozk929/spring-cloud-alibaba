package eking.spring.cloud.gateway.helper;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

@Service
public class DynamicRouteServiceImplByNacos {

    private Logger log = LoggerFactory.getLogger(DynamicRouteServiceImplByNacos.class);
    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    /**
     * 监听Nacos Server下发的动态路由配置
     */
    @PostConstruct
    public void dynamicRouteByNacosListener() {
        try {
            Properties configProperties = new Properties();
            configProperties.put("serverAddr", nacosConfigProperties.getServerAddr());
            configProperties.put("namespace", nacosConfigProperties.getNamespace());
            ConfigService configService = NacosFactory.createConfigService(configProperties);
            configService.getConfig("spring-cloud-gateway-routes.json", "NAOCS-SPRING-CLOUD-GATEWAY", 1000);
            configService.addListener(nacosConfigProperties.getName(), nacosConfigProperties.getGroup(), new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
//                    refreshAllRoute(configInfo);
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (NacosException e) {
            log.error("动态路由配置加载出错。。。", e);
        }
    }

    /**
     * 更新路由配置
     *
     * @param configInfo
     */
    private void refreshAllRoute(String configInfo) {
        if (StringUtils.isBlank(configInfo)) {
            return;
        }
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> configMap = yaml.load(configInfo);
            JSONArray routes = JSONObject.parseObject(JSON.toJSONString(configMap)).getJSONObject("spring")
                    .getJSONObject("cloud").getJSONObject("gateway").getJSONArray("routes");
            List<GatewayRoutesEntity> gatewayRoutesEntityList = JSON.parseArray(routes.toJSONString(), GatewayRoutesEntity.class);
            for (GatewayRoutesEntity gatewayDefine : gatewayRoutesEntityList) {
                RouteDefinition definition = new RouteDefinition();
                definition.setId(gatewayDefine.getId());
                definition.setUri(new URI(gatewayDefine.getUri()));
                List<PredicateDefinition> predicateDefinitions = getPredicateDefinition(gatewayDefine.getPredicates());
                if (predicateDefinitions != null) {
                    definition.setPredicates(predicateDefinitions);
                }
                List<FilterDefinition> filterDefinitions = getFilterDefinition(gatewayDefine.getFilters());
                if (filterDefinitions != null) {
                    definition.setFilters(filterDefinitions);
                }
                dynamicRouteService.update(definition);
            }
        } catch (Exception e) {
            log.error("动态路由配置监听更新出错。。。", e);
        }
    }

    public List<PredicateDefinition> getPredicateDefinition(String predicates) {
        List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
        if (StringUtils.isNotBlank(predicates)) {
            JSONArray predicatesJsonArray = JSONObject.parseArray(predicates);
            if (CollectionUtils.isNotEmpty(predicatesJsonArray)) {
                predicatesJsonArray.forEach(predicate -> {
                    String predicateStr = (String) predicate;
                    PredicateDefinition predicateDefinition = new PredicateDefinition(predicateStr);
                    predicateDefinitionList.add(predicateDefinition);
                });
            }
        }
        return predicateDefinitionList;
    }

    public List<FilterDefinition> getFilterDefinition(String filters) {
        List<FilterDefinition> filterDefinitionList = new ArrayList<>();
        if (StringUtils.isNotBlank(filters)) {
            JSONArray predicatesJsonArray = JSONObject.parseArray(filters);
            if (CollectionUtils.isNotEmpty(predicatesJsonArray)) {
                predicatesJsonArray.forEach(filter -> {
                    String filterStr = (String) filter;
                    FilterDefinition filterDefinition = new FilterDefinition(filterStr);
                    filterDefinitionList.add(filterDefinition);
                });
            }
        }
        return filterDefinitionList;
    }

}