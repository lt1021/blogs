package cn.lt.blog3.base.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lt
 * @date 2021/4/8 9:34
 */
@Data
@ApiModel("最上级实体类")
public class BaseInfo implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    protected Long id;
 }
