package cn.lt.blog3.base.util;

import cn.lt.blog3.base.annotation.SqlWhere;
import cn.lt.blog3.base.sql.SqlField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.blog.base.em.WhereType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lt
 * @date 2021/4/8 10:08
 */
public interface QueryWrapperFormat {

    /**
     * 字段转查询条件
     *
     * @param wrapper
     * @param obj
     * @param sqlField
     * @param <T>
     * @return
     */
    default <T> QueryWrapper<T> format(QueryWrapper<T> wrapper, Object obj, SqlField sqlField) {
//        if (Objects.isNull(sqlField)) {
//            return wrapper;
//        }
//        Field field = sqlField.getField();
//        SqlWhere where = sqlField.getSqlWhere();
//
//        Object value;
//        field.setAccessible(true);
//        try {
//            value = field.get(obj);
//            //获取列名
//            String column = QueryWrapperHelp.parseColumn(sqlField.getColumn());
//            //如果没有使用注解
//            if (Objects.isNull(where)) {
//                //如果字段有值则默认eq
//                if (Objects.nonNull(value)) {
//                    wrapper.eq(column, value);
//                }
//                return wrapper;
//            }
//
//            //如果不构建条件
//            if (!where.where()) {
//                return wrapper;
//            }
//
//            boolean valueIsNotNull = Objects.nonNull(value) && !value.toString().trim().isEmpty();
//            WhereType type = where.type();
//            String innerColumn = QueryWrapperHelp.parseColumn(where.innerColumn());
//
//            switch (type) {
//                case EQ:
//                    return where.innerColumn().isEmpty() ? wrapper.eq(valueIsNotNull, column, value) : wrapper.apply(valueIsNotNull, QueryWrapperHelp.join(column, type, innerColumn));
//                case NE:
//                    return where.innerColumn().isEmpty() ? wrapper.ne(valueIsNotNull, column, value) : wrapper.apply(valueIsNotNull, QueryWrapperHelp.join(column, type, innerColumn));
//                case GT:
//                    return where.innerColumn().isEmpty() ? wrapper.gt(valueIsNotNull, column, value) : wrapper.apply(valueIsNotNull, QueryWrapperHelp.join(column, type, innerColumn));
//                case GE:
//                    return where.innerColumn().isEmpty() ? wrapper.ge(valueIsNotNull, column, value) : wrapper.apply(valueIsNotNull, QueryWrapperHelp.join(column, type, innerColumn));
//                case LT:
//                    return where.innerColumn().isEmpty() ? wrapper.lt(valueIsNotNull, column, value) : wrapper.apply(valueIsNotNull, QueryWrapperHelp.join(column, type, innerColumn));
//                case LE:
//                    return where.innerColumn().isEmpty() ? wrapper.le(valueIsNotNull, column, value) : wrapper.apply(valueIsNotNull, QueryWrapperHelp.join(column, type, innerColumn));
//                case IN:
//                    return !valueIsNotNull
//                            ? wrapper
//                            : wrapper.in(
//                            column,
//                            Arrays.stream(value.toString().split(StringHelp.COMMA)).map(Long::valueOf).collect(Collectors.toList()));
//                case NOT_IN:
//                    return !valueIsNotNull
//                            ? wrapper
//                            : wrapper.notIn(
//                            column,
//                            Arrays.stream(value.toString().split(StringHelp.COMMA)).map(Long::valueOf).collect(Collectors.toList()));
//                case FIND_IN_SET:
//                    return !valueIsNotNull
//                            ? wrapper
//                            : wrapper.apply(String.format("find_in_set(%s, %s)",
//                            where.findInSetIndex() == 1 ? column : QueryWrapperHelp.parseStrValue(value),
//                            where.findInSetIndex() == 1 ? QueryWrapperHelp.parseStrValue(value) : column)
//                    );
//                case NOT_FIND_IN_SET:
//                    return !valueIsNotNull
//                            ? wrapper
//                            : wrapper.apply(String.format("!find_in_set(%s, %s)",
//                            where.findInSetIndex() == 1 ? column : QueryWrapperHelp.parseStrValue(value),
//                            where.findInSetIndex() == 1 ? QueryWrapperHelp.parseStrValue(value) : column)
//                    );
//                case IS_NULL:
//                    return wrapper.isNull(valueIsNotNull, column);
//                case IS_NOT_NULL:
//                    return wrapper.isNotNull(valueIsNotNull, column);
//
//                case LIKE:
//                    if (value instanceof SearchInfo) {
//                        SearchInfo searchInfo = (SearchInfo) value;
//                        if (Objects.nonNull(searchInfo.getSearch()) && !searchInfo.getSearch().trim().isEmpty()
//                                && Objects.nonNull(searchInfo.getSearchClu()) && !searchInfo.getSearchClu().trim().isEmpty()) {
//                            for (String search : searchInfo.getSearch().split(" ")) {
//                                wrapper.and(w -> {
//                                    for (String clu : searchInfo.getSearchClu().split(StringHelp.COMMA)) {
//                                        w.or().like(QueryWrapperHelp.parseColumn(BeanHelp.humpToLine(clu)), search);
//                                    }
//                                });
//                            }
//                        }
//                    }
//                    return wrapper;
//                case GROUP_BY:
//                    return wrapper.groupBy(valueIsNotNull, column);
//            }
//        } catch (Exception e) {
//            System.out.println(field.getName());
//            e.printStackTrace();
//        }
//
//        return wrapper;
        return null;
    }
}
