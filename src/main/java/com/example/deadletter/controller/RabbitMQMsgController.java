package com.example.deadletter.controller;

import com.example.deadletter.components.BusinessMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: wulh
 * @Date: 2020/6/24 18:14
 */
@RequestMapping("rabbitmq")
@RestController
public class RabbitMQMsgController {

    @Autowired
    private BusinessMessageSender sender;

    @RequestMapping("sendmsg")
    public void sendMsg(String msg) {
        sender.sendMsg(msg);
    }
}