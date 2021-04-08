package cn.lt.blog3.controller.base;

import cn.lt.blog3.base.bean.BaseInfo;
import cn.lt.blog3.base.bean.QueryInfo;
import cn.lt.blog3.base.result.PageResult;
import cn.lt.blog3.base.result.ResponseData;
import cn.lt.blog3.base.util.QueryWrapperHelp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 底层抽象类
 * @author lt
 * @date 2021/4/8 9:27
 */
@Validated
@ApiModel("基础Controller")
public abstract class BaseController<T extends BaseInfo, Q extends QueryInfo>  {

    @PostMapping("/insert")
    @ApiOperation(value = "新增")
    public ResponseData insert (@RequestBody @Valid T t){
        getService().save(t);
        return ResponseData.data(t.getId());
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除 true：成功，false：失败")
    public ResponseData delete (@RequestBody @Valid T t){
        return ResponseData.data( getService().removeById(t));
    }

    @PostMapping("/update")
    @ApiOperation(value = "编辑, true：成功，false：失败", response = boolean.class)
    public ResponseData update(@RequestBody @Valid T t){
        return  ResponseData.data(getService().updateById(t));
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询 这个版本不建议使用")
    public ResponseData queryPage(Q q){
        QueryWrapper<T> wrapper = QueryWrapperHelp.formatWrapper(q);
        IPage<T> page = getService().page(q.page(), wrapper);
        return PageResult.data(page.getTotal(), page.getRecords());
    }

    protected  abstract IService<T> getService();

}
