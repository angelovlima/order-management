package br.com.fiap.customer_management.api.customer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Customer {
    private final Long id;
    private String name;
    private String address;
    private String phone;
    private String email;

    public void update(String name, String address, String phone, String email) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }
}
