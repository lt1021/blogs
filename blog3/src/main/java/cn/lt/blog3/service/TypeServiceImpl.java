package cn.lt.blog3.service;

import cn.lt.blog3.api.entity.Tag;
import cn.lt.blog3.api.service.TagService;
import cn.lt.blog3.mapper.TagMapper;
import cn.lt.blog3.service.base.BaseServiceImpl;
import jdk.internal.instrumentation.TypeMapping;
import org.springframework.stereotype.Service;

/**
 * @author lt
 * @date 2021/4/30 18:32
 */
@Service
public class TypeServiceImpl extends BaseServiceImpl<TypeMapping, Tag> implements TagService {
}
