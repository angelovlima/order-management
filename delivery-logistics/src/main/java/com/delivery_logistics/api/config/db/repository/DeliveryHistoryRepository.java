package com.delivery_logistics.api.config.db.repository;

import com.delivery_logistics.api.config.db.entity.DeliveryHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistoryEntity, Long> {
}