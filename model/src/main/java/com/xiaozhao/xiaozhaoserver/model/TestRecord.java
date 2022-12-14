package com.xiaozhao.xiaozhaoserver.model;

import com.baomidou.mybatisplus.annotation.*;
import com.tencentcloudapi.iai.v20200303.models.FaceAttributesInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-13 22:29:17
 * @modify:
 */

@Data
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("测试记录")
@TableName("test_record")
public class TestRecord {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户 id")
    private Integer userId;

    @ApiModelProperty("测试客户端 id")
    private Integer clientId;

    @ApiModelProperty("体温")
    private Double temperature;

    @ApiModelProperty("年龄")
    private Long age;

    @ApiModelProperty("魅力值")
    private Long beauty;

    @ApiModelProperty("心率")
    private Integer heartRate;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDateTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDateTime;

    public TestRecord(FaceAttributesInfo faceAttributesInfo) {
        this.age = faceAttributesInfo.getAge();
        this.beauty = faceAttributesInfo.getBeauty();
        this.temperature = 36.3;
        this.heartRate = 70;
    }
}
