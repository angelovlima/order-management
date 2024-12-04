package com.order_processing.api.order.usecase;

import com.order_processing.api.order.domain.OrderDelivery;
import com.order_processing.api.order.gateway.CustomerGateway;
import com.order_processing.api.order.gateway.OrderGateway;
import com.order_processing.api.order.gateway.ProductGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final CustomerGateway customerGateway;
    private final ProductGateway productGateway;
    private final OrderGateway orderGateway;

    public void execute(OrderDelivery orderDeliveryDTO) {
        customerGateway.validateCustomer(orderDeliveryDTO.idCustomer());

        productGateway.validateProduct(orderDeliveryDTO.idProduct(), orderDeliveryDTO.quantity());

        orderGateway.sendOrder(orderDeliveryDTO);
    }
}
