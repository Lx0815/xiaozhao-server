package com.xiaozhao.xiaozhaoserver.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-09 16:36:00
 * @modify:
 */

@Data
@Accessors(chain = true)
@TableName("person_group")
@ApiModel("人员库")
public class PersonGroup {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("人员库ID")
    private String groupId;

    @ApiModelProperty("客户端ID")
    private Integer clientId;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDateTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDateTime;

}
