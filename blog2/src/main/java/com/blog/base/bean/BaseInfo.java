package com.blog.base.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author lt
 * @date 2021/2/26 15:03
 */

public class BaseInfo implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
