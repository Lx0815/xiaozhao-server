package com.xiaozhao.xiaozhaoserver.config.qiniu;

import com.qiniu.storage.Region;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-11-17 8:53:01
 * @modify:
 */

public interface RegionFactory {

    boolean support(String region);

    Region createRegion();

}
