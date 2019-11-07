package com.stamper.yx.common.controller;

import com.stamper.yx.common.sys.response.ResultVO;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.Set;

/**
 * @author D-wqs
 * @data 2019/10/26 16:56
 */
@RestController
@RequestMapping("/rabbitmq/listener")
public class RabbitMqStart {
    @Resource
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @RequestMapping("stop")
    public ResultVO stop(){
        rabbitListenerEndpointRegistry.stop();
        return ResultVO.OK("已停用rabbitMq监听");
    }

    @RequestMapping("start")
    public ResultVO start(){
        rabbitListenerEndpointRegistry.start();
        return ResultVO.OK("已启用rabbitMq监听");
    }

    @RequestMapping("setup")
    public ResultVO setup(int consumer, int maxConsumer){
        Set<String> containerIds = rabbitListenerEndpointRegistry.getListenerContainerIds();
        SimpleMessageListenerContainer container = null;
        for(String id : containerIds){
            container = (SimpleMessageListenerContainer) rabbitListenerEndpointRegistry.getListenerContainer(id);
            if(container != null){
                container.setConcurrentConsumers(consumer);
                container.setMaxConcurrentConsumers(maxConsumer);
            }
        }
        return ResultVO.OK();
    }
}
