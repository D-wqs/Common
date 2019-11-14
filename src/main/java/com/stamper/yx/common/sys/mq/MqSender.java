package com.stamper.yx.common.sys.mq;

import com.alibaba.fastjson.JSONObject;
import com.stamper.yx.common.sys.error.PrintException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;


@Component
@Configurable
//@EnableScheduling   //启动定时任务
public class MqSender {
	@Autowired
	private AmqpTemplate amqpTemplate;

	/**
	 * 向交换机中推送消息
	 *
	 * @param exchange 交换器
	 * @param obj      消息体
	 */
	public void sendToExchange(String exchange, Object obj) {
		if (obj != null && StringUtils.isNotBlank(exchange)) {
			amqpTemplate.convertAndSend(exchange, "", JSONObject.toJSONString(obj));
			return;
		}
		throw new PrintException("请求参数有误");
	}


	/**
	 * 向队列中推送消息
	 *
	 * @param routek 队列
	 * @param obj    消息体
	 */
	public void sendToQueue(String routek, Object obj) {
		if (obj != null && StringUtils.isNotBlank(routek)) {
			amqpTemplate.convertAndSend(routek, JSONObject.toJSONString(obj));
			return;
		}
		throw new PrintException("请求参数有误");
	}
}