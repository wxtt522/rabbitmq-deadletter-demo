package com.example.deadletter.conf;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: wulh
 * @Date: 2020/6/24 18:05
 */
@Configuration
public class RabbitMQConfig {

    public static final String BUSINESS_EXCHANGE_NAME = "dead.letter.demo.simple.business.exchange";
    public static final String BUSINESS_QUEUE_ROUTING_KEY = "dead.letter.demo.simple.business.queue.routingkey";
    public static final String BUSINESS_QUEUE_NAME = "dead.letter.demo.simple.business.queue";
    public static final String DEAD_LETTER_EXCHANGE = "dead.letter.demo.simple.deadletter.exchange";
    public static final String DEAD_LETTER_QUEUE_ROUTING_KEY = "dead.letter.demo.simple.deadletter.queue.routingkey";
    public static final String DEAD_LETTER_QUEUE_NAME = "dead.letter.demo.simple.deadletter.queue";

//     声明业务Exchange
    @Bean("businessExchange")
    public TopicExchange businessExchange() {
        return new TopicExchange(BUSINESS_EXCHANGE_NAME);
    }

    // 声明死信Exchange
    @Bean("deadLetterExchange")
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    // 声明死信队列
    @Bean("deadLetterQueue")
    public Queue deadLetterQueueA() {
        return new Queue(DEAD_LETTER_QUEUE_NAME);
    }

    // 声明业务队列A，绑定私信交换机与路由
    @Bean("businessQueue")
    public Queue businessQueue() {
        Map<String, Object> args = new HashMap<>(2);
//       x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
//       x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_ROUTING_KEY);
        return QueueBuilder.durable(BUSINESS_QUEUE_NAME).withArguments(args).build();
    }


    // 声明业务队列绑定关系
    @Bean
    public Binding businessBinding(@Qualifier("businessQueue") Queue queue,
                                    @Qualifier("businessExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(BUSINESS_QUEUE_ROUTING_KEY);
    }

    // 声明死信队列绑定关系
    @Bean
    public Binding deadLetterBinding(@Qualifier("deadLetterQueue") Queue queue,
                                      @Qualifier("deadLetterExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_LETTER_QUEUE_ROUTING_KEY);
    }

}