package com.raqamiEcommerce.EcommerceShop.request;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String address;
}
