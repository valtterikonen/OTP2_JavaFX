package com.example.easybankproject.controllers;

import com.example.easybankproject.models.User;
import com.example.easybankproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getUserData(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        User user = userService.getUserData(jwtToken);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/me")
    public ResponseEntity<User> updateUserData(@RequestHeader("Authorization") String token, @RequestBody User updatedUser) {
        String jwtToken = token.substring(7);
        User user = userService.updateUserData(jwtToken, updatedUser);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String result = userService.registerUser(user);

        if (result == null || result.equals("Username already exists.")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username already exists.");
        }
        return ResponseEntity.ok(result);
    }

   @PostMapping("/login")
   public ResponseEntity<String> loginUser(@RequestBody User user) {
       String result = userService.loginUser(user);
       if (result == null || result.equals("Invalid username or password.")) {
           return ResponseEntity
                   .status(HttpStatus.UNAUTHORIZED)
                   .contentType(MediaType.TEXT_PLAIN)
                   .body("Invalid username or password.");
       }
       return ResponseEntity
               .ok()
               .contentType(MediaType.TEXT_PLAIN)
               .body(result);
   }

}

