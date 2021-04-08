package cn.lt.blog3.base.util;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;

/**
 * @author lt
 * @date 2021/4/8 14:26
 */
@Data
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class Msg {
    private Locale locale;
}
