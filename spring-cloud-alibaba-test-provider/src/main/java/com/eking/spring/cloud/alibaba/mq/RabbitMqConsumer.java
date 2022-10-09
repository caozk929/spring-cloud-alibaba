package com.eking.spring.cloud.alibaba.mq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

/**
 * @author caozk  caozhaokui
 * @version 1.00.00
 * @since
 */
@Service
public class RabbitMqConsumer implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println("收到消息：" + new String(message.getBody()));
    }
}
