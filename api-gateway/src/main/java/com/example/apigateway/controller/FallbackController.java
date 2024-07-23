package com.example.apigateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @Operation(summary = "User Service Fallback", description = "This endpoint is called when the User Service is unavailable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Service fallback response")
    })
    @GetMapping("/user")
    public String userServiceFallback() {
        return "User Service is currently unavailable. Please try again later.";
    }

    @Operation(summary = "Product Service Fallback", description = "This endpoint is called when the Product Service is unavailable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product Service fallback response")
    })
    @GetMapping("/product")
    public String productServiceFallback() {
        return "Product Service is currently unavailable. Please try again later.";
    }

    @Operation(summary = "Order Service Fallback", description = "This endpoint is called when the Order Service is unavailable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order Service fallback response")
    })
    @GetMapping("/order")
    public String orderServiceFallback() {
        return "Order Service is currently unavailable. Please try again later.";
    }

    @Operation(summary = "Payment Service Fallback", description = "This endpoint is called when the Payment Service is unavailable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment Service fallback response")
    })
    @GetMapping("/payment")
    public String paymentServiceFallback() {
        return "Payment Service is currently unavailable. Please try again later.";
    }
}
