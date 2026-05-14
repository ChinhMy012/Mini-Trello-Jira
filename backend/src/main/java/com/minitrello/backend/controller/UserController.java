package com.minitrello.backend.controller;

import com.minitrello.backend.dto.UserCreationRequest;
import com.minitrello.backend.dto.UserResponse;
import com.minitrello.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody @Valid UserCreationRequest request) {
        return userService.createUser(request);
    }
}