package cn.lt.blog3.entity;

import cn.lt.blog3.base.bean.BaseInfo;
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

    @ApiModelProperty(value = "标题")
    private Boolean appreciation;

    @ApiModelProperty(value = "标题")
    private Integer shareStatement;

    @ApiModelProperty(value = "标题")
    private Integer commentabled;

    @ApiModelProperty(value = "标题")
    private Integer published;

    @ApiModelProperty(value = "标题")
    private Integer recommend;

    @ApiModelProperty(value = "标题")
    private Date createTime;

    @ApiModelProperty(value = "标题")
    private Date updateTime;

    @ApiModelProperty(value = "标题")
    private Long typeId;

    @ApiModelProperty(value = "标题")
    private Long userId;

    @ApiModelProperty(value = "标题")
    private String description;

    @ApiModelProperty(value = "标题")
    private String tagIds;

    @ApiModelProperty(value = "标题")
    private String firstPocture;


}
