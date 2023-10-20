package com.activeMQ.reqRes.consumer;

import com.activeMQ.reqRes.entity.Entity;
import com.activeMQ.reqRes.repository.Repo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@RestController
public class Consumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "standalone.queue")
    public void consume(String message){
        System.out.println("Received Messages: " + message);
    }

    @GetMapping("/request/{request}")
    public String sendRequest(@PathVariable("request") String request) {
        // Send the request
        jmsTemplate.convertAndSend("RequestQueue", request);

        // Receive the response
        return (String) jmsTemplate.receiveAndConvert("ResponseQueue");
    }

    @PostMapping("/request/add")
    public String getAll(@RequestBody Entity entity) throws JsonProcessingException {
        // Convert the Entity object to a JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(entity);

        // Send the request
        jmsTemplate.convertAndSend("RequestQueue", json);

        // Receive the response
        return (String) jmsTemplate.receiveAndConvert("ResponseQueue");
    }

}
