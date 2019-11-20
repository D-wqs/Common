package com.stamper.yx.common.controller;

import com.stamper.yx.common.entity.User;
import com.stamper.yx.common.service.UserService;
import com.stamper.yx.common.sys.cache.EHCacheGlobal;
import com.stamper.yx.common.sys.cache.EHCacheUtil;
import com.stamper.yx.common.sys.jwt.JwtUtil;
import com.stamper.yx.common.sys.md5.MD5;
import com.stamper.yx.common.sys.myconfig.IgnorePath;
import com.stamper.yx.common.sys.response.Code;
import com.stamper.yx.common.sys.response.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 注册用户成功后，该用户会生成一个企业凭证：MD5(uuid)+"_"+userId;
 * 管理页面需要登陆，获取用户信息后，获取该凭证（签名后得到的token）。
 * 第三方调用接口,直接使用该凭证token，放在请求参数里，调用其它接口
 * <p>
 * 例：用户登录：点击获取token，直接拿走该token使用，调用第三方接口，
 * 加入要更新接口凭证：在后管登陆，点击获取会更新token
 */

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    //    mvc登陆方法
    @RequestMapping(value = "/getTicket",method = RequestMethod.POST)
    @ResponseBody
    public String getTicket(String name, String password) {
        if (StringUtils.isAnyBlank(name, password)) {
            log.info("【getTicket:0】参数有误：name{{}},password{{}}", name, password);
            return "0";
        }

        User user = userService.getUser(name);
        if (user == null) {
            log.info("【getTicket:1】当前用户不存在name:{{}}", name);
            return "1";
        }
        String s = MD5.toMD5(password);
        if(!s.equalsIgnoreCase(user.getPassword())){
            log.info("【getTicket:2】密码错误");
            return "2";
        }
        String accesstoken = user.getAccesstoken();
        return accesstoken;

    }

    @RequestMapping(value = "login",method = RequestMethod.POST)
    public String loginV1(@RequestParam("name") String name,
                          @RequestParam("password") String password,
                          HttpSession session, Map<String, Object> map) {
        if (StringUtils.isAnyBlank(name, password)) {

            map.put("msg", "用户名或密码不能为空");
            return "login";
        }
        User user = userService.getUser(name);
        if (user != null) {
            String password1 = user.getPassword();
            String s = MD5.toMD5(password);
            if (password1.equals(s)) {
                //密码正确
                session.setAttribute("loginUser", name);
                session.setAttribute("accessToken", user.getAccesstoken());
                return "redirect:/yunxi/main.html";
            }
            //密码不正确
            map.put("msg", "密码错误");
            return "login";
        }
        //返回登陆页,当前用户不存在
        map.put("msg", "当前用户不存在");
        return "login";
    }

    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResultVO getUser(@RequestParam("name") String name) {
        if (!StringUtils.isEmpty(name)) {
            User user = userService.getUser(name);
            if (user != null) {
                String key = EHCacheGlobal.USER_TOKEN_KEY + user.getId();
                Object o = EHCacheUtil.get(key);
                log.info("通过key-->{{}},获取到缓存内容value：-->{{}}", key, o.toString());
                boolean validate = JwtUtil.validate(o.toString(), user.getPassword());
                if (validate == false) {
                    return ResultVO.FAIL(Code.FAIL_TOKEN);
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", user.getId());
                map.put("name", user.getName());
                return ResultVO.OK(map);
            }
        }
        return ResultVO.FAIL(Code.ERROR_PARAMETER);
    }

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResultVO addUser(User user) {
        if (user != null) {
            String name = user.getName();
            String password = user.getPassword();
            if (StringUtils.isAnyEmpty(name, password)) {
                return ResultVO.FAIL(Code.ERROR_PARAMETER);
            }
            User user_bak = userService.getUser(name);
            if (user_bak != null) {
                return ResultVO.OK("当前用户已注册");
            }
            String s = MD5.toMD5(password);
            user.setPassword(s);
            userService.add(user);
            //配置企业凭证 MD5(uuid)+"_"+userId;
            String s1 = UUID.randomUUID().toString();
            String replace = s1.replace("-", "");
            String s2 = MD5.toMD5(replace) + "_" + user.getId();
            user.setAccesstoken(s2);
            userService.update(user);
            log.info("注册后的用户信息" + user.toString());
            return ResultVO.OK();
        }
        return ResultVO.FAIL(Code.ERROR_PARAMETER);
    }

    /**
     * 得到request对象
     */
    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return request;
    }
}
