package cn.lt.blog3.base.result;

/**
 * 分页响应数据
 * @author lt
 * @date 2021/4/8 9:56
 */
public class PageResult extends ResponseData {

    /**
     * 总数
     */
    private long total;

    public static PageResult data(long total, Object list) {
        PageResult result = new PageResult();
        result.setTotal(total);
        result.setData(list);
        return result;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
