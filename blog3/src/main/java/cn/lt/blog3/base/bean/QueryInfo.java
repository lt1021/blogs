package cn.lt.blog3.base.bean;

import cn.lt.blog3.base.annotation.SqlWhere;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.base.util.BeanHelp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lt
 * @date 2021/4/8 9:38
 */
@Data
public class QueryInfo {

    @ApiModelProperty("是否根据id进行过滤，传则过滤。")
    private Long id;

    @SqlWhere(where = false)
    @ApiModelProperty("页码，默认1。")
    private Integer pageNo = 1;

    @SqlWhere(where = false)
    private Integer startLimit;

    @SqlWhere(where = false)
    @ApiModelProperty("页大小，默认50。")
    private Integer pageSize = 50;

    @SqlWhere(where = false)
    @ApiModelProperty("排序信息")
    private List<OrderItem> orders = new ArrayList<>();

    @SqlWhere(where = false)
    @ApiModelProperty("查询的字段")
    private String select;


    public <T> IPage<T> page() {
        Page<T> page = new Page<>();
        page.setOrders(validOrder());
        if (Objects.isNull(this.pageNo) || Objects.isNull(this.pageSize)) {
            return page;
        }
        page.setCurrent(this.pageNo).setSize(this.pageSize)
                .setOrders(this.orders);
        return page;
    }


    /**
     * 排序
     * @return
     */
    private List<OrderItem> validOrder() {
        List<OrderItem> list = orders.stream()
                .filter(Objects::nonNull)
                .filter(d -> Objects.nonNull(d.getColumn()))
                .filter(d -> d.getColumn().matches("^[0-9a-zA-Z]+$"))
                .collect(Collectors.toList());
        list.forEach(d -> d.setColumn(String.format("`%s`", BeanHelp.humpToLine(d.getColumn()))));
        return list;
    }
}
