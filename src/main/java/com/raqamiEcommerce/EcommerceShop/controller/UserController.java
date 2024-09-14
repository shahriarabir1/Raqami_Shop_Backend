package com.raqamiEcommerce.EcommerceShop.controller;

import com.raqamiEcommerce.EcommerceShop.dto.UserDto;
import com.raqamiEcommerce.EcommerceShop.exception.AlreadyExistsException;
import com.raqamiEcommerce.EcommerceShop.exception.UserNotFoundException;
import com.raqamiEcommerce.EcommerceShop.model.Role;
import com.raqamiEcommerce.EcommerceShop.model.User;
import com.raqamiEcommerce.EcommerceShop.request.CreateUserRequest;
import com.raqamiEcommerce.EcommerceShop.request.UpdateUserRequest;
import com.raqamiEcommerce.EcommerceShop.response.ApiResponse;
import com.raqamiEcommerce.EcommerceShop.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable("userId") Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDto userDto=userService.convertUsertoDto(user);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("User Not Found!", e.getMessage()));
        }
    }

    @PostMapping("/user/create")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest user,@RequestParam(required = false) String role) {
        try {
            User users = userService.createUser(user,role);
            return ResponseEntity.ok(new ApiResponse("Success", users));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT)
                    .body(new ApiResponse("User Already Exist", e.getMessage()));
        }
    }

    @PutMapping("/{userId}/user/update")
    public ResponseEntity<ApiResponse> putUser(@RequestBody UpdateUserRequest user,@PathVariable Long userId) {
        try {
            User users = userService.updateUser(user,userId);
            UserDto userDto=userService.convertUsertoDto(users);
            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(CONFLICT)
                    .body(new ApiResponse("User not found!", e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/user/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(CONFLICT)
                    .body(new ApiResponse("User Already Exist", e.getMessage()));
        }
    }

}
