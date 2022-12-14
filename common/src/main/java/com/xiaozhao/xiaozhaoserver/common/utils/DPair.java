package com.xiaozhao.xiaozhaoserver.common.utils;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-09 8:58:25
 * @modify:
 */

@Data
@Accessors(chain = true)
public class DPair<T, U> {

    private T key;

    private U value;

    public void clear() {
        key = null;
        value = null;
    }

    public DPair<T, U> setKeyValue(T key, U value) {
        this.key = key;
        this.value = value;
        return this;
    }

}
