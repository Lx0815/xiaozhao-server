package com.xiaozhao.xiaozhaoserver.service.config.qiniu;

import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-11-17 9:03:35
 * @modify:
 */

public class RegionFactoryBuilder {

    private static final ServiceLoader<RegionFactory> regionFactories = ServiceLoader.load(RegionFactory.class);

    private static final Map<String, RegionFactory> factoryMap = new HashMap<>();

    private RegionFactoryBuilder() {
    }

    public static RegionFactory builder(String region) throws NoSuchProviderException {
        RegionFactory f = factoryMap.get(region);
        if (Objects.nonNull(f)) return f;
        for (RegionFactory factory : regionFactories) {
            if (factory.support(region)) {
                factoryMap.put(region, factory);
                return factory;

            }
        }
        throw new NoSuchProviderException("Region 配置错误，没有程序能够支持 " + region);
    }
}