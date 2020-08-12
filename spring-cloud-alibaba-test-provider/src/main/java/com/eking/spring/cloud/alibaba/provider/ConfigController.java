package com.eking.spring.cloud.alibaba.provider;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @SentinelResource(value = "getUseLocalCache")
    @RequestMapping("/getUseLocalCache")
    public boolean get() {
        return useLocalCache;
    }


}