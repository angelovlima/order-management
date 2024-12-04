package com.delivery_logistics.api.config.db.repository;

import com.delivery_logistics.api.config.db.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
}
