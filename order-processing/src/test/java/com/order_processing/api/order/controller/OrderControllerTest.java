package com.order_processing.api.order.controller;

import com.order_processing.api.order.domain.OrderDelivery;
import com.order_processing.api.order.usecase.CreateOrderUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateOrderUseCase createOrderUseCase;

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {
        doNothing().when(createOrderUseCase).execute(Mockito.any(OrderDelivery.class));

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "idCustomer": 123,
                                    "idProduct": 456,
                                    "quantity": 10,
                                    "paymentMethod": "CREDIT_CARD",
                                    "orderStatus": "PENDING"
                                }
                                """))
                .andExpect(status().isOk());
    }
}
