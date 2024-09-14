package com.raqamiEcommerce.EcommerceShop.service.user;

import com.raqamiEcommerce.EcommerceShop.dto.UserDto;
import com.raqamiEcommerce.EcommerceShop.exception.AlreadyExistsException;
import com.raqamiEcommerce.EcommerceShop.exception.UserNotFoundException;
import com.raqamiEcommerce.EcommerceShop.model.Role;
import com.raqamiEcommerce.EcommerceShop.model.User;
import com.raqamiEcommerce.EcommerceShop.repository.UserRepository;
import com.raqamiEcommerce.EcommerceShop.request.CreateUserRequest;
import com.raqamiEcommerce.EcommerceShop.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest request, String role) {

        return Optional.of(request)
                .filter(user-> !userRepository.existsByEmail(user.getEmail()))
                .map(req->{
                    User user = new User();
                    if(role==null){
                        Role newRole=new Role("ROLE_USER");
                        user.setRoles(List.of(newRole));
                    }else{
                        Role newRole=new Role(role);
                        user.setRoles(List.of(newRole));
                    }
                    user.setEmail(req.getEmail());
                    user.setPassword(passwordEncoder.encode(req.getPassword()));
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    user.setAddress(req.getAddress());
                    user.setPhone(req.getPhone());
                    return userRepository.save(user);
                }).orElseThrow(()->new AlreadyExistsException("Opps!" +request.getEmail()+"Already Exists"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setAddress(request.getAddress());
                    user.setPhone(request.getPhone());
                    return userRepository.save(user);
                }).orElseThrow(()->new UserNotFoundException("User Not Found"));

    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete,
                        ()->{throw new UserNotFoundException("User not found");});
    }
    @Override
    public UserDto convertUsertoDto(User user){
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public User getAuthenticatedUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
