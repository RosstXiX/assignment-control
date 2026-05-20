package io.github.rosstxix.assignmentcontrol.feature.auth.service;

import io.github.rosstxix.assignmentcontrol.feature.auth.dto.LoginRequest;
import io.github.rosstxix.assignmentcontrol.feature.auth.dto.LoginResponse;
import io.github.rosstxix.assignmentcontrol.feature.auth.dto.PasswordChangeRequest;
import io.github.rosstxix.assignmentcontrol.feature.employee.domain.Account;
import io.github.rosstxix.assignmentcontrol.feature.employee.repository.AccountRepository;
import io.github.rosstxix.assignmentcontrol.infrastructure.error.exception.BusinessApiException;
import io.github.rosstxix.assignmentcontrol.infrastructure.security.SecurityUser;
import io.github.rosstxix.assignmentcontrol.infrastructure.security.SecurityUtils;
import io.github.rosstxix.assignmentcontrol.infrastructure.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(token, "Bearer", jwtService.getExpirationSeconds());
    }

    @Transactional
    public void changePassword(PasswordChangeRequest request) {
        String currentUsername = SecurityUtils.getCurrentUsername();
        Account account = accountRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new BusinessApiException("Account not found"));

        if (!passwordEncoder.matches(request.oldPassword(), account.getPassword())) {
            throw new BusinessApiException("Invalid old password");
        }

        account.setPassword(passwordEncoder.encode(request.newPassword()));
        accountRepository.save(account);
    }
}
