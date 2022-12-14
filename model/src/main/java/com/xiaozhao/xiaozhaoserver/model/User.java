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
 * @createTime: 2022-12-07 22:42:41
 * @modify:
 */

@Data
@ApiModel("人员")
@Accessors(chain = true)
@TableName("user")
public class User {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("人员 ID（UUID）")
    private String personId;

    @ApiModelProperty("微信号")
    private String wechatId;

    @ApiModelProperty("人员昵称")
    private String personName;

    @ApiModelProperty("人员性别")
    private String gender;

    @ApiModelProperty("人员真实年龄")
    private String realAge;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDateTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDateTime;

}
