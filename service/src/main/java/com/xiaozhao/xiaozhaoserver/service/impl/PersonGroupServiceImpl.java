package com.xiaozhao.xiaozhaoserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaozhao.xiaozhaoserver.mapper.PersonGroupMapper;
import com.xiaozhao.xiaozhaoserver.model.PersonGroup;
import com.xiaozhao.xiaozhaoserver.service.PersonGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-28 23:17:00
 * @modify:
 */

@Service
@Transactional
public class PersonGroupServiceImpl extends ServiceImpl<PersonGroupMapper, PersonGroup> implements PersonGroupService {



}
