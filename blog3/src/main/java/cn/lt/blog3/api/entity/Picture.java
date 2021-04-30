package cn.lt.blog3.api.entity;

import cn.lt.blog3.base.bean.BaseInfo;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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
@ApiModel(value="TPicture对象", description="")
public class Picture extends BaseInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "照片地址")
    private String pictureaddress;

    @ApiModelProperty(value = "照片描述")
    private String picturedescription;

    @ApiModelProperty(value = "照片名字")
    private String picturename;

    @ApiModelProperty(value = "照片时间")
    private Date picturetime;

    @ApiModelProperty(value = "图片路径")
    @TableField("imagePath")
    private String imagepath;


}
