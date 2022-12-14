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
 * @createTime: 2022-12-08 14:22:54
 * @modify:
 */

@Data
@ApiModel("人脸表")
@Accessors(chain = true)
@TableName("person_face")
public class PersonFace {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("映射到 user.id")
    private Integer userId;

    @ApiModelProperty("人脸 ID（UUID)")
    private String faceId;

    @ApiModelProperty("人脸图片链接")
    private String imageUrl;

    @ApiModelProperty("人脸图片质量（0 - 100分）")
    private Long imageQualityScore;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDateTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDateTime;
}
