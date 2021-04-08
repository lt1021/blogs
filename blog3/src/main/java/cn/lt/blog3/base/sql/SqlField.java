package cn.lt.blog3.base.sql;

import cn.lt.blog3.base.annotation.SqlWhere;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author lt
 * @date 2021/4/8 10:11
 */
@Data
public class SqlField {

    private String column;

    private Field field;

    private SqlWhere sqlWhere;
}
