package com.order_processing.api.order.usecase;

import com.order_processing.api.order.domain.OrderDelivery;
import com.order_processing.api.order.domain.PaymentMethod;
import com.order_processing.api.order.exception.CustomerNotFoundException;
import com.order_processing.api.order.exception.ProductValidationException;
import com.order_processing.api.order.gateway.CustomerGateway;
import com.order_processing.api.order.gateway.OrderGateway;
import com.order_processing.api.order.gateway.ProductGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateOrderUseCaseTest {

    @Mock
    private CustomerGateway customerGateway;

    @Mock
    private ProductGateway productGateway;

    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    private OrderDelivery orderDelivery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderDelivery = new OrderDelivery(
                123L,
                456L,
                10,
                PaymentMethod.CREDIT_CARD,
                "PENDING"
        );
    }

    @Test
    void shouldExecuteSuccessfully() {
        createOrderUseCase.execute(orderDelivery);

        verify(customerGateway).validateCustomer(orderDelivery.idCustomer());
        verify(productGateway).validateProduct(orderDelivery.idProduct(), orderDelivery.quantity());
        verify(orderGateway).sendOrder(orderDelivery);
    }

    @Test
    void shouldThrowExceptionWhenCustomerIsInvalid() {
        doThrow(new CustomerNotFoundException("Cliente não encontrado"))
                .when(customerGateway).validateCustomer(orderDelivery.idCustomer());

        assertThrows(CustomerNotFoundException.class, () -> createOrderUseCase.execute(orderDelivery));
        verify(customerGateway).validateCustomer(orderDelivery.idCustomer());
        verifyNoInteractions(productGateway);
        verifyNoInteractions(orderGateway);
    }

    @Test
    void shouldThrowExceptionWhenProductIsInvalid() {
        doThrow(new ProductValidationException("Produto inválido"))
                .when(productGateway).validateProduct(orderDelivery.idProduct(), orderDelivery.quantity());

        assertThrows(ProductValidationException.class, () -> createOrderUseCase.execute(orderDelivery));
        verify(customerGateway).validateCustomer(orderDelivery.idCustomer());
        verify(productGateway).validateProduct(orderDelivery.idProduct(), orderDelivery.quantity());
        verifyNoInteractions(orderGateway);
    }
}
