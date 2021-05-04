package cn.lt.blog3.api.entity;

import cn.lt.blog3.base.bean.BaseInfo;
import com.baomidou.mybatisplus.annotation.TableName;
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
@ApiModel(value="TTag对象", description="")
@TableName("t_tag")
public class Tag extends BaseInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String name;


}
