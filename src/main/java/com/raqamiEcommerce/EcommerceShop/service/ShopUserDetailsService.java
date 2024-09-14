package com.raqamiEcommerce.EcommerceShop.service;

import com.raqamiEcommerce.EcommerceShop.exception.UserNotFoundException;
import com.raqamiEcommerce.EcommerceShop.model.User;
import com.raqamiEcommerce.EcommerceShop.repository.UserRepository;
import com.raqamiEcommerce.EcommerceShop.security.user.ShopUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShopUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user= Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(()->new UsernameNotFoundException("Username Not Found!"));
        return ShopUserDetails.buildUserDetails(user);
    }
}
