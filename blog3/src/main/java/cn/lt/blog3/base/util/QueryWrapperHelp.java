package cn.lt.blog3.base.util;

import cn.lt.blog3.base.annotation.SqlWhere;
import cn.lt.blog3.base.bean.QueryInfo;
import cn.lt.blog3.base.sql.SqlField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.base.util.BeanHelp;
import com.blog.base.util.SysAssert;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lt
 * @date 2021/4/8 10:07
 */
public class QueryWrapperHelp {

    private final static QueryWrapperFormat DEFAULT_FORMAT = new QueryWrapperFormat() {
    };

    /**
     * 缓存
     */
    private final static Map<Class<?>, Map<String, SqlField>> QUERY_CACHE = new HashMap<>();

    /**
     * 将query转为Wrapper
     *
     * @param q   查询参数
     * @param <T> 查询结果类型
     * @param <Q> 查询参数对象
     * @return
     */
    public static <T, Q extends QueryInfo> QueryWrapper<T> formatWrapper(Q q) {
        return formatWrapper(q, DEFAULT_FORMAT);
    }

    /**
     * 将query转为Wrapper
     *
     * @param q      查询参数
     * @param format 自定义格式化处理，没有则传null。
     * @param <T>    查询结果类型
     * @param <Q>    查询参数对象
     * @return
     */
    public static <T, Q extends QueryInfo> QueryWrapper<T> formatWrapper(Q q, QueryWrapperFormat format) {
        SysAssert.notNull("params_error", format);
        QueryWrapper<T> wrapper = parse(q, format);
        wrapper.select(q.getSelect());
        return wrapper;
    }

    /**
     * 将对象非null字段转化为查询字段
     *
     * @param obj
     * @param format
     * @param <T>
     * @return
     */
    public static <T> QueryWrapper<T> parse(Object obj, QueryWrapperFormat format) {
        SysAssert.notNull("params_error", format);

        Map<String, SqlField> cacheMap = cache(obj);

        List<Map.Entry<String, SqlField>> orList = cacheMap.entrySet().stream()
                .filter(Objects::nonNull)
                .filter(entry -> Objects.nonNull(entry.getValue().getSqlWhere()))
                .filter(entry -> entry.getValue().getSqlWhere().isOr())
                .collect(Collectors.toList());

        QueryWrapper<T> wrapper = new QueryWrapper<>();

        List<String> fieldList = new ArrayList<>(cacheMap.size());
        for (Map.Entry<String, SqlField> entry : orList) {
            SqlField sqlField = entry.getValue();
            fieldList.add(entry.getKey());
            Field field = sqlField.getField();
            Object value = BeanHelp.getFieldValue(obj, field);
            if (Objects.nonNull(value)) {
                SqlWhere sqlWhere = sqlField.getSqlWhere();
                String[] orFiled = sqlWhere.orFiled();

                wrapper.and(wra ->
                        wra.and(and -> {
                            for (String fieldName : orFiled) {
                                fieldList.add(fieldName);
                                SqlField andSqlFiled = cacheMap.get(fieldName);
                                format.format(and, obj, andSqlFiled);
                            }
                        }).or(or -> format.format(or, obj, sqlField))
                );
            }
        }

        cacheMap.entrySet().stream()
                .filter(d -> !fieldList.contains(d.getKey()))
                .forEach(entry -> format.format(wrapper, obj, entry.getValue()));
        return wrapper;
    }

    /**
     * 缓存
     * @param obj
     * @return
     */
    private static Map<String, SqlField> cache(Object obj) {
        return QUERY_CACHE.computeIfAbsent(obj.getClass(), key -> {
            List<Field> fields = BeanHelp.getAllFields(obj.getClass());
            Map<String, SqlField> cacheMap = new HashMap<>(fields.size());
            for (Field field : fields) {
                SqlField sqlField = new SqlField();
                sqlField.setField(field);
                SqlWhere sqlWhere = field.getAnnotation(SqlWhere.class);
                sqlField.setSqlWhere(sqlWhere);
                String column = Objects.isNull(sqlWhere) || StringHelp.isEmpty(sqlWhere.column())
                        ? BeanHelp.humpToLine(field.getName())
                        : sqlWhere.column();
                sqlField.setColumn(column);
                cacheMap.put(field.getName(), sqlField);
            }
            return cacheMap;
        });
    }
}
