package cn.lt.blog3.controller;

import cn.lt.blog3.base.em.ResponseStatus;
import cn.lt.blog3.base.result.ResponseData;
import cn.lt.blog3.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 登陆
 * @author lt
 * @date 2021/4/8 12:05
 */
@RestController
public class LoginController {

    /**
     * 手机号登陆
     * @param phone
     * @param password
     * @return
     */
    @GetMapping("loginUser")
    public ResponseData loginUser(@RequestParam("phone") String phone, @RequestParam("password")String password
                                , HttpSession session){
        UsernamePasswordToken token = new UsernamePasswordToken(phone, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            User user  = (User) subject.getPrincipal();
            user.setPassword("");
            session.setAttribute("user",user);
            return ResponseData.data();
        }catch (AuthenticationException e){
            return ResponseData.data(ResponseStatus.FAIL,"登陆失败");
        }

    }
}
