package com.example.deadletter.components;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.deadletter.conf.RabbitMQConfig.BUSINESS_QUEUE_NAME;

/**
 * @Description:
 * @Author: wulh
 * @Date: 2020/6/24 18:08
 */
@Slf4j
@Component
public class BusinessMessageReceiver {

    @RabbitListener(queues = BUSINESS_QUEUE_NAME)
    public void receive(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        System.out.println("收到业务消息：" + msg);
        boolean ack = true;
        Exception exception = null;
        try {
            if (msg.contains("deadletter")) {
                throw new RuntimeException("dead letter exception");
            }
        } catch (Exception e) {
            ack = false;
            exception = e;
        }
        if (!ack) {
            log.error("消息消费发生异常，error msg:{}", exception.getMessage());
            //当返回nack或refuse消息或超出最大重发次数时，消息将从业务队列删除转发至绑定的死信息队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        } else {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}