package com.stamper.yx.common.controller;

import com.stamper.yx.common.entity.Config;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.service.ConfigService;
import com.stamper.yx.common.service.SignetService;
import com.stamper.yx.common.service.mysql.MysqlSignetService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.response.Code;
import com.stamper.yx.common.sys.response.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class SignetMVCController {
    @Autowired
    private SignetService signetService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private MysqlSignetService mysqlSignetService;

    @GetMapping("/yunxi/signetList")
    public String list(Model model) {
        List<Signet> all = signetService.getAll();
        if (all != null && all.size() > 0) {
            model.addAttribute("signets", all);
            return "signets/signetList";
        }
        //TODO 去往错误页面
        return "redirect:error.html";
    }

    /**
     * 退出
     *
     * @param session
     * @return
     */
    @GetMapping("/signOut")
    public String signOut(HttpSession session) {
        session.removeAttribute("loginUser");
        return "redirect:/index.html";
    }

    //印章列表中，通过设备id获取设备信息,这里的接口需要登陆管理后台才能修改
    @PostMapping("/yunxi/getSignetConfigById")
    @ResponseBody
    public ResultVO getSignetInfoById(Integer id) {
        //前台点击设备详情，获取设备的配置信息
        if (id == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        Signet signet = signetService.get(id);
        if (signet == null || StringUtils.isBlank(signet.getUuid())) {
            return ResultVO.FAIL("当前设备不存在");
        }
        //通过uuid查询配置信息，没有查到就是用默认配置
        Config byUUID = configService.getByUUID(signet.getUuid());
        if (byUUID != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", signet.getName());
            map.put("config", byUUID);
            return ResultVO.OK(map);
        } else {
            Config defaultConfig = configService.getDefaultConfig();
            defaultConfig.setUuid(signet.getUuid());
            configService.insert(defaultConfig);
            Map<String, Object> map = new HashMap<>();
            map.put("title", signet.getName());
            map.put("config", defaultConfig);
            return ResultVO.OK(map);

        }
    }

    /**
     * mysql数据源获取所有印章
     *
     * @param model
     * @return
     */
    @GetMapping("/yunxi/MysqlsignetList")
    public String Mysqllist(Model model) {
        String openMysql = AppConstant.OPEN_MYSQL;
        if (StringUtils.isBlank(openMysql) || "false".equalsIgnoreCase(openMysql)) {
            return "redirect:/error.html";
        }
        List<Signet> all = mysqlSignetService.getAll();
        if (all != null && all.size() > 0) {
            model.addAttribute("mysqlSignets", all);
            return "signets/mysqlSignetList";
        }
        //TODO 去往错误页面
        return "redirect:/error.html";
    }

    @PostMapping("/yunxi/getMysqlSignetById")
    @ResponseBody
    public ResultVO getMysqlSignetById(Integer id) {
        if (id == null) {
            return ResultVO.FAIL(Code.ERROR_PARAMETER);
        }
        if (mysqlSignetService == null) {
            return ResultVO.FAIL(Code.ERROR500);
        }
        Signet byId = mysqlSignetService.getById(id);
        if (byId != null) {
            return ResultVO.OK(byId);
        }
        return null;
    }


}
