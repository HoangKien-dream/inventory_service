package com.example.productservice.config;

import com.example.productservice.entity.Product;
import com.example.productservice.enums.Status;
import com.example.productservice.repository.RepositoryProduct;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.example.productservice.config.MessageConfig.QUEUE_INVENTORY;
import static com.example.productservice.config.MessageConfig.QUEUE_PAY;

@Component
public class RecieveMessage {
    @Autowired
    RepositoryProduct repositoryProduct;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = {QUEUE_INVENTORY})
    public void getMessage(String message) {
        List<Product> list = new ArrayList<>();
        Gson gson = new Gson();
        OrderEvent orderEvent = gson.fromJson(message, OrderEvent.class);
        orderEvent.orderDetailEvents.forEach(orderDetailEvent -> {
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
        });
        try {
//            repositoryProduct.saveAll(list);
            orderEvent.setStatusOrder(Status.InventoryStatus.DONE.name());
//            rabbitTemplate.convertAndSend(MessageConfig.DIRECT_EXCHANGE, MessageConfig.DIRECT_ROUTING_KEY_ORDER, orderEvent);
        } catch (Exception e) {
            orderEvent.setStatusOrder(Status.InventoryStatus.PENDING.name());
//            rabbitTemplate.convertAndSend(MessageConfig.DIRECT_EXCHANGE, MessageConfig.DIRECT_ROUTING_KEY_ORDER, orderEvent);
        }
    }
}
