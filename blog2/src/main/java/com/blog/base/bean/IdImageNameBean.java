package com.blog.base.bean;

import com.blog.base.util.GlobalUtil;
import com.blog.base.util.ThreadMapUtil;

/**
 * @author lt
 * @date 2021/3/5 11:26
 */
public class IdImageNameBean {
    private Long id;
    private String picture;
    private String name;

    public IdImageNameBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPictureDate() {
        return GlobalUtil.getUnixTime();
    }

    public Long getPictureStaffId() {
        return ThreadMapUtil.getStaffId();
    }
}
