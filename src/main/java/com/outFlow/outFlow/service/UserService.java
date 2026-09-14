package com.outFlow.outFlow.service;

import com.outFlow.outFlow.entity.User;
import com.outFlow.outFlow.repository.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User processOAuthUser(OAuth2User oauthUser) {

        String googleId = oauthUser.getAttribute("sub");
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        return userRepository.findByGoogleId(googleId)
                .map(existingUser -> {

                    existingUser.setName(name);
                    existingUser.setPicture(picture);

                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {

                    User user = new User();

                    user.setGoogleId(googleId);
                    user.setEmail(email);
                    user.setName(name);
                    user.setPicture(picture);
                    user.setCurrency("INR");

                    return userRepository.save(user);
                });
    }

    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}
