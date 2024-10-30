package com.example.easybankproject.services;

import com.example.easybankproject.db.UserRepository;
import com.example.easybankproject.models.User;
import com.example.easybankproject.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BankAccountService bankAccountService;

    public User getUserData(String token) {
        String username = jwtUtil.extractUsername(token);
        return userRepository.findByUsername(username);
    }

    public User updateUserData(String token, User updatedUser) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username);

        if (user != null) {
            user.setEmail(updatedUser.getEmail());
            user.setAddress(updatedUser.getAddress());
            user.setPhonenumber(updatedUser.getPhonenumber());
            user.setFirstname(updatedUser.getFirstname());
            user.setLastname(updatedUser.getLastname());
            userRepository.save(user);
        }

        return user;
    }

    public String registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "Username already exists.";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        bankAccountService.createBankAccount(user);

        String token = jwtUtil.generateToken(user.getUsername());
        notificationService.createNotification(user, null, user.getUsername() + " has registered successfully.");
        return token;
    }

    public String loginUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return "Invalid username or password.";
        }

        String token = jwtUtil.generateToken(user.getUsername());
        notificationService.createNotification(existingUser, null, user.getUsername() + " has logged in.");
        return token;
    }
}