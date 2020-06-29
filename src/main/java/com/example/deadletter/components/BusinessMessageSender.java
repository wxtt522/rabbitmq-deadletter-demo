package com.example.deadletter.components;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.deadletter.conf.RabbitMQConfig.BUSINESS_EXCHANGE_NAME;
import static com.example.deadletter.conf.RabbitMQConfig.BUSINESS_QUEUE_ROUTING_KEY;


/**
 * @Description:
 * @Author: wulh
 * @Date: 2020/6/24 18:13
 */
@Component
public class BusinessMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMsg(String msg){
        rabbitTemplate.convertSendAndReceive(BUSINESS_EXCHANGE_NAME, BUSINESS_QUEUE_ROUTING_KEY, msg);
    }
}