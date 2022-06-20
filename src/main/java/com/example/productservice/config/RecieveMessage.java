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
import static com.example.productservice.enums.Status.PaymentStatus.PENDING;

@Component
public class RecieveMessage {
    @Autowired
    ConsumerService consumerService;
    @RabbitListener(queues = {QUEUE_INVENTORY})
    public void getMessage(OrderEvent orderEvent) {
        orderEvent.setQueueName("QUEUE_INVENTORY");
      if (orderEvent.getStatusInventory().equals("PENDING")){
          consumerService.getMessage(orderEvent);
      }
      if (orderEvent.getStatusPayment().equals(Status.PaymentStatus.NOT_ENOUGH_BALANCE.name())){
          consumerService.getMessageReturn(orderEvent);
      }
    }
}
