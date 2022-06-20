package com.example.productservice.config;

import com.example.productservice.entity.Product;
import com.example.productservice.enums.Status;
import com.example.productservice.repository.RepositoryProduct;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.productservice.config.MessageConfig.QUEUE_INVENTORY;

@Service
public class ConsumerService {

    @Autowired
    RepositoryProduct repositoryProduct;
    @Autowired
    RabbitTemplate rabbitTemplate;
    public void getMessage(OrderEvent orderEvent){
        List<Product> list = new ArrayList<>();
//        Gson gson = new Gson();
//        OrderEvent orderEvent = gson.fromJson(message, OrderEvent.class);
        for (OrderDetailEvent orderDetailEvent:
             orderEvent.getOrderDetailEvents()) {
            Product product = repositoryProduct.findById(orderDetailEvent.getProductId()).orElse(null);
            if (orderDetailEvent.getQuantity() <= product.getQuantity()) {
                int quantity = product.getQuantity() - orderDetailEvent.getQuantity();
                product.setQuantity(quantity);
                list.add(product);
            } else {
                orderEvent.setStatusInventory(Status.InventoryStatus.OUT_OF_STOCK.name());
                rabbitTemplate.convertAndSend(MessageConfig.DIRECT_EXCHANGE, MessageConfig.DIRECT_ROUTING_KEY_ORDER, orderEvent);
                return;
            }
        }
        try {
            repositoryProduct.saveAll(list);
            orderEvent.setStatusInventory(Status.InventoryStatus.DONE.name());
            rabbitTemplate.convertAndSend(MessageConfig.DIRECT_EXCHANGE, MessageConfig.DIRECT_ROUTING_KEY_ORDER, orderEvent);
        } catch (Exception e) {
            orderEvent.setStatusInventory(Status.InventoryStatus.PENDING.name());
            rabbitTemplate.convertAndSend(MessageConfig.DIRECT_EXCHANGE, MessageConfig.DIRECT_ROUTING_KEY_ORDER, orderEvent);
        }
    }

    public void getMessageReturn(OrderEvent orderEvent){
        List<Product> list = new ArrayList<>();
//        Gson gson = new Gson();
//        OrderEvent orderEvent = gson.fromJson(message, OrderEvent.class);
        orderEvent.orderDetailEvents.forEach(orderDetailEvent -> {
            Product product = repositoryProduct.findById(orderDetailEvent.getProductId()).orElse(null);
            int quantity = product.getQuantity() + orderDetailEvent.getQuantity();
            product.setQuantity(quantity);
            list.add(product);
        });
        try {
            repositoryProduct.saveAll(list);
            orderEvent.setStatusInventory(Status.InventoryStatus.RETURNED.name());
            rabbitTemplate.convertAndSend(MessageConfig.DIRECT_EXCHANGE, MessageConfig.DIRECT_ROUTING_KEY_ORDER, orderEvent);
        } catch (Exception e) {
            orderEvent.setStatusInventory(Status.InventoryStatus.RETURNED.name());
            rabbitTemplate.convertAndSend(MessageConfig.DIRECT_EXCHANGE, MessageConfig.DIRECT_ROUTING_KEY_ORDER, orderEvent);
        }
    }
}
