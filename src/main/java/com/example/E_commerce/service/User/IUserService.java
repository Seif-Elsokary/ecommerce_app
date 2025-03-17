package com.example.E_commerce.service.User;

import com.example.E_commerce.Entity.User;
import com.example.E_commerce.dto.UserDto;
import com.example.E_commerce.request.CreateUserRequest;
import com.example.E_commerce.request.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest user);
    User updateUser(UpdateUserRequest user , Long userId);
    void deleteUser(Long userId);

    User getAuthenticatedUser();

    UserDto convertToDto(User user);
}
