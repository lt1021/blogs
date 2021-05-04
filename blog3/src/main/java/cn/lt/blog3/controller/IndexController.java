package cn.lt.blog3.controller;


import cn.lt.blog3.base.bean.BaseInfo;
import cn.lt.blog3.base.bean.QueryInfo;
import cn.lt.blog3.base.result.PageResult;
import cn.lt.blog3.base.result.ResponseData;
import cn.lt.blog3.controller.base.BaseController;
import cn.lt.blog3.api.entity.Blog;
import cn.lt.blog3.api.service.BlogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 * @author lt
 * @date 2021/4/7 17:56
 */
@RestController
public class IndexController extends BaseController<Blog,QueryInfo> {
    @Autowired
    private BlogService service;

    static {
        System.out.println("测试");
    }
    @GetMapping("/")
    public ResponseData index(Model model) {
        return ResponseData.data(service.queryPage() );
    }

    @ApiOperation(value = "搜索")
    @PostMapping("/search")
    public ResponseData search(){
        return ResponseData.data();
    }

    @Override
    protected IService getService() {
        return service;
    }
}
