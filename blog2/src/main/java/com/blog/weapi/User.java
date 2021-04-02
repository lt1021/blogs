package com.blog.weapi;

import java.io.Serializable;

/**
 * @author xtiger (xtiger@microsoul.com) 2010-6-18 15:39:04
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userid;
    private String password;

    private String name;
    private User owner;

    private int lft;
    private int rgt;
    private int depth;

    private String settings;
    private String lang;
    private String bindIps;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getLft() {
        return lft;
    }

    public void setLft(int lft) {
        this.lft = lft;
    }

    public int getRgt() {
        return rgt;
    }

    public void setRgt(int rgt) {
        this.rgt = rgt;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }


    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getLang() {
        return lang == null ? "zh_CN" : lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getBindIps() {
        return bindIps;
    }

    public void setBindIps(String bindIps) {
        this.bindIps = bindIps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userid != null && userid.equals(user.userid);
    }

    @Override
    public int hashCode() {
        return userid != null ? userid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("userid=").append(userid).append(",name=")
                .append(name).append(",owner=").append(owner == null ? null : owner.getUserid()).append(",lft=")
                .append(lft).append(",rgt=").append(rgt)
                .append(",depth=")
                .append(depth)
                .append(",settings=")
                .append(settings).append("\n")
                .toString();
    }
}
