package cn.lt.blog3.service;

import cn.lt.blog3.api.entity.Tag;
import cn.lt.blog3.api.entity.Type;
import cn.lt.blog3.api.service.TagService;
import cn.lt.blog3.api.service.TypeService;
import cn.lt.blog3.mapper.TagMapper;
import cn.lt.blog3.mapper.TypeMapper;
import cn.lt.blog3.service.base.BaseServiceImpl;
import jdk.internal.instrumentation.TypeMapping;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lt
 * @date 2021/4/30 18:32
 */
@Service
public class TypeServiceImpl extends BaseServiceImpl<TypeMapper, Type> implements TypeService {

    @Override
    public List<Type> getBlogType() {
        return baseMapper.getBlogType();
    }
}
