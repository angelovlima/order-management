package com.order_processing.api.config;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class OrderProperties {
    private String orderCreatedChannel = "order-created-supplier";
    private String orderUpdatedChannel = "order-updated-supplier";
}
