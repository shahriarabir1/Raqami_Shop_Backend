package com.raqamiEcommerce.EcommerceShop.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtResponse {
    private Long id;
    private String token;
}
