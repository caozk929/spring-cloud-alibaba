package com.eking.spring.cloud.alibaba.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ${DESCRIPTION}
 *
 * @author caozhaokui
 * @create 2017-11-27 16:33
 */
@Component
public class RabbitMsgSender implements MsgSender<String, String> {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void sendMsg(String exchange, String routekey, String key, String data, int delay) {
        amqpTemplate.convertAndSend(exchange, routekey, data, message -> {
            if (delay != 0) {
                message.getMessageProperties().setDelay(delay);
            }
            return message;
        });
    }

}
