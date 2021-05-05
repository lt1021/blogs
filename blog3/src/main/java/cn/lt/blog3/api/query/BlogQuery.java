package cn.lt.blog3.api.query;

import cn.lt.blog3.base.bean.QueryInfo;
import lombok.Data;

/**
 * @author lt
 * @date 2021/5/5 11:58
 */
@Data
public class BlogQuery extends QueryInfo {
    private String blogName;
}
