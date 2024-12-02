package com.order_processing.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class RabbitMQConfig {
//    @Value("${queue.order.name}")
//    private String orderQueueName;
//
//    @Value("${queue.order.exchange.name}")
//    private String orderExchangeName;
//
//    @Value("${queue.order.routing.key}")
//    private String orderRoutingKey;
//
////    Criação da fila de pedidos.
//    @Bean
//    public Queue orderQueue() {
//        return new Queue(orderQueueName, true); // true = fila durável
//    }
//
//
////    Criação da exchange de pedidos.
//    @Bean
//    public TopicExchange orderExchange() {
//        return new TopicExchange(orderExchangeName);
//    }
//
////    Vinculação da fila à exchange com uma routing key.
//    @Bean
//    public Binding orderBinding(Queue orderQueue, TopicExchange orderExchange) {
//        return BindingBuilder.bind(orderQueue).to(orderExchange).with(orderRoutingKey);
//    }
//}


