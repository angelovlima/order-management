package com.delivery_logistics.api.model.entity;

import com.delivery_logistics.api.model.enums.OrderStatus;
import com.delivery_logistics.api.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_customer", nullable = false)
    private Long idCustomer;

    @Column(name = "id_product", nullable = false)
    private Long idProduct;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    public Delivery(Long idCustomer, Long idProduct, Integer quantity, PaymentMethod paymentMethod, OrderStatus orderStatus) {
        this.idCustomer = idCustomer;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
    }
}