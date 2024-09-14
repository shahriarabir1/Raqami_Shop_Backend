package com.raqamiEcommerce.EcommerceShop.service.user;

import com.raqamiEcommerce.EcommerceShop.dto.UserDto;
import com.raqamiEcommerce.EcommerceShop.model.User;
import com.raqamiEcommerce.EcommerceShop.request.CreateUserRequest;
import com.raqamiEcommerce.EcommerceShop.request.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);

    User createUser(CreateUserRequest request, String role);

    User updateUser(UpdateUserRequest request, Long userId);

    void deleteUser(Long userId);

    UserDto convertUsertoDto(User user);

    User getAuthenticatedUser();
}
