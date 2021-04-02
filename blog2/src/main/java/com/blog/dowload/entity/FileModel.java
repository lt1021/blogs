package com.blog.dowload.entity;

/**
 * @author lt
 * @date 2021/3/5 11:58
 */
public class FileModel {
    private String id;
    private String name;
    private String path;
    private int status;

    public FileModel() {
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public int getStatus() {
        return this.status;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof FileModel)) {
            return false;
        } else {
            FileModel other = (FileModel)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$id = this.getId();
                Object other$id = other.getId();
                if (this$id == null) {
                    if (other$id != null) {
                        return false;
                    }
                } else if (!this$id.equals(other$id)) {
                    return false;
                }

                Object this$name = this.getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }

                Object this$path = this.getPath();
                Object other$path = other.getPath();
                if (this$path == null) {
                    if (other$path != null) {
                        return false;
                    }
                } else if (!this$path.equals(other$path)) {
                    return false;
                }

                if (this.getStatus() != other.getStatus()) {
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof FileModel;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return "FileModel(id=" + this.getId() + ", name=" + this.getName() + ", path=" + this.getPath() + ", status=" + this.getStatus() + ")";
    }
}
