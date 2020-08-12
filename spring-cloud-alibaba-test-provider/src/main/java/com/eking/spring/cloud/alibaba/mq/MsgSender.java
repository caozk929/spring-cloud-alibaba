package com.eking.spring.cloud.alibaba.mq;

/**
 * ${DESCRIPTION}
 *
 * @author caozhaokui
 * @create 2017-10-17 15:13
 */
public interface MsgSender<K,V> {
    void sendMsg(String exchange, String topic, K key, V data, int delay);
}
