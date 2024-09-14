package com.raqamiEcommerce.EcommerceShop.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}
