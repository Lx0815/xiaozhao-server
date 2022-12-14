package com.xiaozhao.xiaozhaoserver.config.qiniu.impl;

import com.qiniu.storage.Region;
import com.xiaozhao.xiaozhaoserver.config.qiniu.RegionFactory;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-11-17 9:39:41
 * @modify:
 */

public class AutoRegionFactory implements RegionFactory {

    private static final String SUPPORT_REGION = "auto";

    @Override
    public boolean support(String region) {
        return SUPPORT_REGION.equalsIgnoreCase(region);
    }

    @Override
    public Region createRegion() {
        return Region.autoRegion();
    }

    public Region createRegion(String ucServer) {
        return Region.autoRegion(ucServer);
    }
}
