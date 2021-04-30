package cn.lt.blog3.api.entity;

import cn.lt.blog3.base.bean.BaseInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lt
 * @since 2021-04-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Type对象", description="")
public class Type extends BaseInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;


}
