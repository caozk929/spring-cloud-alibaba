package com.eking.spring.cloud.alibaba.mq;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/msg")
public class MqController {
    @Autowired
    private RabbitMsgSender rabbitMsgSender;

    @RequestMapping("/sendMsg")
    public void sendMsg(String msg) {
        rabbitMsgSender.sendMsg("hweb.fanout.exchange", "", "orderId", msg, 1000);
    }


}