package com.stamper.yx.common.sys.myconfig.filter;

import com.alibaba.fastjson.JSONObject;
import com.stamper.yx.common.service.OrderLogService;
import com.stamper.yx.common.sys.security.AES.AesUtil;
import com.stamper.yx.common.websocket.core.DefaultWebSocket;
import com.stamper.yx.common.websocket.core.ObjectMessage;
import com.stamper.yx.common.websocket.handle.SendMessageFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhf_10@163.com
 * @Description 对websocket通道发送的数据进行RSA加密
 * @date 2019/8/23 0023 8:52
 */
@Slf4j
@Component
public class RSASendMessageFilter implements SendMessageFilter {
	@Autowired
	private OrderLogService orderLogService;

	@Override
	public boolean isAccess(Object message, DefaultWebSocket socket) {
		if (message == null) {
			//发送的消息体为空，则返回false
			return false;
		}

		if (StringUtils.isBlank(message.toString())) {
			//发送的消息体为空，则返回false
			return false;
		}
		try {
			ObjectMessage om = (ObjectMessage) message;
			byte[] decryptStr = null;
			if (om.getData() instanceof String) {
				//如果发送的消息是字符串,直接取字节数组
				decryptStr = om.getData().toString().getBytes();
			} else {
				//如果发送的消息非字符串,转json后,取字节数组
				decryptStr = JSONObject.toJSONString(om.getData()).getBytes();
			}
			if (decryptStr != null && decryptStr.length > 0) {
				String s = new String(decryptStr);
				if (!"pong".equalsIgnoreCase(s)) {
					//使用私钥加密后,将密文赋值给消息
					log.info("【对称加密】发送数据前（明文）：" + om.getData());
					om.setData(AesUtil.encrypt(s, socket.getSymmetricKey()));
					log.info("【对称加密】发送数据前（密文）：" + om.getData());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//不管加密失败还是成功,都返回ture
		return true;
	}
}
