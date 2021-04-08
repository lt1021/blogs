package cn.lt.blog3.controller;


import cn.lt.blog3.base.bean.BaseInfo;
import cn.lt.blog3.base.bean.QueryInfo;
import cn.lt.blog3.base.result.ResponseData;
import cn.lt.blog3.controller.base.BaseController;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 * @author lt
 * @date 2021/4/7 17:56
 */
@RestController
public class IndexController<T extends BaseInfo, Q extends QueryInfo> extends BaseController<T,Q> {

    @GetMapping("/")
    public ResponseData index() {
        return ResponseData.data();
    }

    @ApiOperation(value = "搜索")
    @PostMapping("/search")
    public ResponseData search(){
        return ResponseData.data();
    }

    @Override
    protected IService<T> getService() {
        return null;
    }
}
