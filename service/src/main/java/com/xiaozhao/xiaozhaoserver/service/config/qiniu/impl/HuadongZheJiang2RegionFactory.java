package com.xiaozhao.xiaozhaoserver.service.config.qiniu.impl;

import com.qiniu.storage.Region;
import com.xiaozhao.xiaozhaoserver.service.config.qiniu.RegionFactory;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-11-17 9:34:34
 * @modify:
 */

public class HuadongZheJiang2RegionFactory implements RegionFactory {

    private static final String SUPPORT_REGION = "huadongzhejiang2";

    @Override
    public boolean support(String region) {
        return SUPPORT_REGION.equalsIgnoreCase(region);
    }

    @Override
    public Region createRegion() {
        return Region.huadongZheJiang2();
    }
}
