package com.example.orderservice.service;

import com.example.orderservice.client.PaymentClient;
import com.example.orderservice.client.PaymentRequest;
import com.example.orderservice.client.PaymentResponse;
import com.example.orderservice.client.ProductClient;
import com.example.orderservice.client.ProductResponse;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.exception.OutOfStockException;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private PaymentClient paymentClient;

    private static final Logger logger = Logger.getLogger(OrderService.class.getName());

    public Order createOrder(CreateOrderRequest createOrderRequest) {
        Map<Long, Integer> productQuantities = new HashMap<>();
        Map<Long, String> productNames = new HashMap<>();
        double totalAmount = 0;

        try {
            for (CreateOrderRequest.ProductOrder productOrder : createOrderRequest.getProducts()) {
                Long productId = productOrder.getProductId();
                Integer quantity = productOrder.getQuantity();

                ProductResponse product = productClient.getProductById(productId);

                if (product.getStock() == 0) {
                    throw new OutOfStockException("Product " + product.getName() + " is out of stock.");
                } else if (product.getStock() < quantity) {
                    throw new OutOfStockException("There are only " + product.getStock() + " quantities available of product " + product.getName() + ".");
                }

                totalAmount += product.getPrice() * quantity;
                productClient.reduceStock(productId, quantity);

                productQuantities.put(productId, quantity);
                productNames.put(productId, product.getName());
            }

            Order order = new Order();
            order.setUserId(createOrderRequest.getUserId());
            order.setProductQuantities(productQuantities);
            order.setProductNames(productNames);
            order.setTotalAmount(totalAmount);
            order.setOrderStatus("NEW");
            order.setOrderDate(LocalDateTime.now());
            Order savedOrder = orderRepository.save(order);

            // Process Payment
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setOrderId(savedOrder.getId());
            paymentRequest.setAmount(totalAmount);
            paymentRequest.setPaymentMethod(createOrderRequest.getPaymentMethod());

            PaymentResponse paymentResponse = paymentClient.processPayment(paymentRequest);

            if (!"SUCCESS".equals(paymentResponse.getPaymentStatus())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment processing failed");
            }

            return savedOrder;
        } catch (ResponseStatusException e) {
            throw e; // Return the exception to be handled by the controller
        } catch (Exception e) {
            logger.severe("Error creating order: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create order", e);
        }
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
