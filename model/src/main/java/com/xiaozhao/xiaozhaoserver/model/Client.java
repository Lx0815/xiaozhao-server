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
 * @createTime: 2022-12-06 21:44:46
 * @modify:
 */
@Data
@ApiModel("客户端")
@Accessors(chain = true)
@TableName("client")
public class Client {
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("客户端Id（UUID）")
    @TableField(fill = FieldFill.INSERT)
    private String clientId;

    @ApiModelProperty("映射到 client_location.id ")
    private Integer locationId;

    @ApiModelProperty("人员库 ID（UUID）")
    private Integer personGroupId;

    @ApiModelProperty("上次上传时间")
    private LocalDateTime lastUploadDateTime;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDateTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDateTime;
}
