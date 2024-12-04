package com.delivery_logistics.api.config.db.entity;

import com.delivery_logistics.api.delivery.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "delivery_history")
public class DeliveryHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private DeliveryEntity delivery;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public DeliveryHistoryEntity(DeliveryEntity delivery, OrderStatus status, LocalDateTime changedAt, String notes) {
        this.delivery = delivery;
        this.status = status;
        this.changedAt = changedAt;
        this.notes = notes;
    }
}