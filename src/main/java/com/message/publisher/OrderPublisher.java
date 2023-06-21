package com.message.publisher;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.config.MessagingConfig;
import com.entity.Order;
import com.entity.OrderStatus;

@RestController
@RequestMapping("/order")
public class OrderPublisher {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@PostMapping("/{rest}")
	String publishOrder(@RequestBody Order order, @PathVariable String rest) {
		order.setId(UUID.randomUUID().toString());
		OrderStatus orderStatus = new OrderStatus(order, "PROCESSING",
				"Order to " + rest + " has been placed successfully");
		rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, orderStatus);
		return "Order placed successfully";
	}

}
