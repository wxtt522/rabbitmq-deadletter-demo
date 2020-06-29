package com.example.deadletter.components;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.deadletter.conf.RabbitMQConfig.DEAD_LETTER_QUEUE_NAME;

/**
 * @Description:
 * @Author: wulh
 * @Date: 2020/6/24 18:12
 */
@Component
public class DeadLetterMessageReceiver {


    @RabbitListener(queues = DEAD_LETTER_QUEUE_NAME)
    public void receive(Message message, Channel channel) throws IOException {
        System.out.println("收到死信消息：" + new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}