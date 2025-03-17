package com.example.E_commerce.service.User;

import com.example.E_commerce.Entity.User;
import com.example.E_commerce.Exceptions.AlreadyExistsFoundException;
import com.example.E_commerce.Exceptions.UserNotFoundException;
import com.example.E_commerce.dto.UserDto;
import com.example.E_commerce.repository.UserRepository;
import com.example.E_commerce.request.CreateUserRequest;
import com.example.E_commerce.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
         return userRepository.findById(userId)
                 .orElseThrow(()-> new UserNotFoundException("user with id: "+userId + " Not Found!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(user.getEmail()))
                .map(req-> {
                    User user1 = new User();
                    user1.setFirstName(req.getFirstName());
                    user1.setLastName(req.getLastName());
                    user1.setEmail(req.getEmail());
                    user1.setPassword(passwordEncoder.encode(req.getPassword()));
                    return userRepository.save(user1);
                }).orElseThrow(()-> new AlreadyExistsFoundException(request.getEmail() + " Already Exists!"));
    }

    @Override
    public User updateUser(UpdateUserRequest user, Long userId) {
        return userRepository.findById(userId)
                .map(existingUser ->{
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    return userRepository.save(existingUser);
                }).orElseThrow(()-> new UserNotFoundException("user with id: "+userId + " Not Found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete , ()->{
                    throw new UserNotFoundException("user with id: "+userId + " Not Found!");
                });
    }

    @Override
    public User getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder
                                .getContext().getAuthentication();

        String email = auth.getName();
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
