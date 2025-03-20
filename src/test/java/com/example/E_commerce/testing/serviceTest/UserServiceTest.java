package com.example.E_commerce.testing.serviceTest;

import com.example.E_commerce.Exceptions.UserNotFoundException;
import com.example.E_commerce.repository.UserRepository;
import com.example.E_commerce.request.CreateUserRequest;
import com.example.E_commerce.request.UpdateUserRequest;
import com.example.E_commerce.service.User.UserService;
import com.example.E_commerce.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPassword("password");

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setFirstName("John");
        createUserRequest.setLastName("Doe");
        createUserRequest.setEmail("john@example.com");
        createUserRequest.setPassword("password");
    }

    @Test
    void getUserById_ShouldReturnSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(userRepository , times(1)).findById(1L);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserDoesNotExist() {

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class , ()->userService.getUserById(2L));
        verify(userRepository , times(1)).findById(2L);

    }


    @Test
    void updateUser_ShouldUpdateSuccessfully() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setFirstName("saif");
        updateUserRequest.setLastName("soliman");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class))).thenReturn(user);

        User updateUser = userService.updateUser(updateUserRequest , 1L);

        assertNotNull(updateUser);
        assertEquals("saif", updateUser.getFirstName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserDoesNotExist() {

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(updateUserRequest, 1L));
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    void deleteUser_ShouldDeleteSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.deleteUser(1L);
        verify(userRepository, times(1)).delete(user);
    }


    @Test
    void deleteUser_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(2L));
        verify(userRepository, never()).delete(any(User.class));
    }
}
