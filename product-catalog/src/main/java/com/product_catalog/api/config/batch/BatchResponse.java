package com.product_catalog.api.config.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BatchResponse {
    private String message;
    private String jobName;
    private String status;
}
