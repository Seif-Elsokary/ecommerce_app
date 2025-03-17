package com.example.E_commerce.security.user;

import com.example.E_commerce.Entity.User;
import com.example.E_commerce.Exceptions.UserNotFoundException;
import com.example.E_commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(()-> new UserNotFoundException("user not found!"));

        return ShopUserDetails.buildUserDetails(user);
    }
}
