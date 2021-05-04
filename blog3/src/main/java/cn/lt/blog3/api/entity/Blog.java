package cn.lt.blog3.api.entity;

import cn.lt.blog3.base.bean.BaseInfo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@ApiModel(value="TBlog对象", description="")
@TableName("t_blog")
public class Blog extends BaseInfo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "首图")
    private String firstPicture;

    @ApiModelProperty(value = "标记")
    private String flag;

    @ApiModelProperty(value = "浏览次数")
    private Integer views;

    @ApiModelProperty(value = "赞赏开启")
    private Boolean appreciation;

    @ApiModelProperty(value = "版权开启")
    private Integer shareStatement;

    @ApiModelProperty(value = "评论开启")
    private Integer commentabled;

    @ApiModelProperty(value = "发布")
    private Integer published;

    @ApiModelProperty(value = "推荐")
    private Integer recommend;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "类型id")
    @TableField("type_id")
    private Long typeId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "标签id")
    @TableField("tag_ids")
    private String tagIds;




}
