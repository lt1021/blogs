package com.blog.base.bean;

import com.blog.base.util.GlobalUtil;
import com.blog.base.util.ThreadMapUtil;

/**
 * @author lt
 * @date 2021/3/5 11:23
 */
public class ImageBean {
    private String code;
    private String picture;
    private String thumb;

    public ImageBean() {
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getThumb() {
        return this.thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public long getPictureDate() {
        return GlobalUtil.getUnixTime();
    }

    public Long getPictureStaffId() {
        return ThreadMapUtil.getStaffId();
    }
}
