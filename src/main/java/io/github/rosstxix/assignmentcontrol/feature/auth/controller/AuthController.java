package io.github.rosstxix.assignmentcontrol.feature.auth.controller;

import io.github.rosstxix.assignmentcontrol.feature.auth.dto.LoginRequest;
import io.github.rosstxix.assignmentcontrol.feature.auth.dto.LoginResponse;
import io.github.rosstxix.assignmentcontrol.feature.auth.dto.PasswordChangeRequest;
import io.github.rosstxix.assignmentcontrol.feature.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    @PutMapping("/password")
    public void changePassword(@RequestBody @Valid PasswordChangeRequest request) {
        authService.changePassword(request);
    }
}
