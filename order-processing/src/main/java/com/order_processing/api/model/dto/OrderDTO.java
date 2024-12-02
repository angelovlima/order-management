package com.order_processing.api.model.dto;

import com.order_processing.api.model.enums.OrderStatus;
import com.order_processing.api.model.enums.PaymentMethod;

import java.util.List;

public record OrderDTO(
        Long id,
        CustomerDTO customerDTO,
        List<ProductDTO> productDTOList,
        PaymentMethod paymentMethod,
        OrderStatus orderStatus
) {
}
