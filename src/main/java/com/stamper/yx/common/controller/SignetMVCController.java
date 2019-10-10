package com.stamper.yx.common.controller;

import com.stamper.yx.common.entity.Config;
import com.stamper.yx.common.entity.Signet;
import com.stamper.yx.common.service.ConfigService;
import com.stamper.yx.common.service.SignetService;
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
        if(signet==null|| StringUtils.isBlank(signet.getUuid())){
            return ResultVO.FAIL("当前设备不存在");
        }
        Config byUUID = configService.getByUUID(signet.getUuid());
        if (byUUID != null) {
            Map<String,Object>map=new HashMap<>();
            map.put("title",signet.getName());
            map.put("config",byUUID);
            return ResultVO.OK(map);
        }else{
            Config defaultConfig = configService.getDefaultConfig();
            defaultConfig.setUuid(signet.getUuid());
            configService.insert(defaultConfig);
            Map<String,Object>map=new HashMap<>();
            map.put("title",signet.getName());
            map.put("config",defaultConfig);
            return ResultVO.OK(map);

        }
    }

}
