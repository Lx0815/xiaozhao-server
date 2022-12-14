package com.xiaozhao.xiaozhaoserver.config.qiniu.impl;

import com.qiniu.storage.Region;
import com.xiaozhao.xiaozhaoserver.config.qiniu.RegionFactory;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-11-17 9:29:00
 * @modify:
 */

public class HuadongRegionFactory implements RegionFactory {

    private static final String SUPPORT_REGION = "huadong";

    @Override
    public boolean support(String region) {
        return SUPPORT_REGION.equalsIgnoreCase(region);
    }

    @Override
    public Region createRegion() {
        return Region.huadong();
    }
}
