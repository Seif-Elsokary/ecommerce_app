package com.example.E_commerce.controller;

import com.example.E_commerce.Entity.User;
import com.example.E_commerce.Exceptions.UserNotFoundException;
import com.example.E_commerce.dto.UserDto;
import com.example.E_commerce.request.CreateUserRequest;
import com.example.E_commerce.request.UpdateUserRequest;
import com.example.E_commerce.response.ApiResponse;
import com.example.E_commerce.service.User.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final IUserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(new ApiResponse("get User By Id Successfully.", userDto));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest user) {
        try {
            User createdUser = userService.createUser(user);
            UserDto userDto = userService.convertToDto(createdUser);
            return ResponseEntity.ok(new ApiResponse("Successfully created user", userDto));
        }catch (UserNotFoundException u){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(u.getMessage(), null));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest user,@PathVariable Long userId) {
        try {
            User updatedUser = userService.updateUser(user, userId);
            UserDto userDto = userService.convertToDto(updatedUser);
            return ResponseEntity.ok(new ApiResponse("Successfully updated user", userDto));
        }catch (UserNotFoundException u){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(u.getMessage(), null));
        }
    }
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUserById(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("delete User By Id Successfully.", null));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
        }
    }

}
