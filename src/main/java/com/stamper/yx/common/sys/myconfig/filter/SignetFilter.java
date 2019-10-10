package com.stamper.yx.common.sys.myconfig.filter;

import com.alibaba.fastjson.JSONObject;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.jwt.JwtUtil;
import com.stamper.yx.common.sys.response.Code;
import com.stamper.yx.common.sys.response.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SignetFilter extends OncePerRequestFilter{
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.info("指令过滤器");
//        获取path
        String requestURI = httpServletRequest.getRequestURI();
        log.info("请求path:{{}}",requestURI);
        List<String> ignorePath=new ArrayList<>();
        ignorePath.add("/device/getAccessToken");
        ignorePath.add("/device/ws");//websocket地址
        ignorePath.add("/device/deviceCallBack/res");//设备回调到本地的地址
        ignorePath.add("/device/config/addConfigError");//设备日志上传
        ignorePath.add("/device/config/updateAPK");//更新apk
        ignorePath.add("/device/sealRecordInfo/addEasyInfo");//记录上传
        ignorePath.add("/device/sealRecordInfo/addNormalInfo");
        ignorePath.add("/device/sealRecordInfo/addAuditInfo");
        ignorePath.add("/device/config/getConfigByUUID");//获取配置
        ignorePath.add("/device/config/updateConfigByUUID");//更新设备配置
        ignorePath.add("/device/deviceCallBack/moduleCallback");//模块回调请求本地不拦截
        if(!ignorePath.contains(requestURI)){
            String token = httpServletRequest.getHeader(AppConstant.ACCESSTOKEN_KEY_PRIFIX);
            System.out.println("请求头：" + token);
            if (token == null|| StringUtils.isBlank(token)) {
                httpServletResponse.setContentType("text/html;charset=utf-8");
                ResultVO fail = ResultVO.FAIL(Code.FAIL_TOKEN);
                String s = JSONObject.toJSONString(fail);
                httpServletResponse.getWriter().print(s);
                return;
            }
            //校验token：是否过期
            boolean validate = JwtUtil.validate(token, AppConstant.TOKEN_KEY);
            if (validate == false) {
                log.error("凭证校验失败");
                httpServletResponse.setContentType("text/html;charset=utf-8");
                ResultVO fail = ResultVO.FAIL(Code.FAIL_TOKEN);
                String s = JSONObject.toJSONString(fail);
                httpServletResponse.getWriter().print(fail);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
