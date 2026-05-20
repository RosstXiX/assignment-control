package io.github.rosstxix.assignmentcontrol.infrastructure.security;

import io.github.rosstxix.assignmentcontrol.feature.employee.domain.Account;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails {
    private final Account account;
    @Getter
    private final Integer employeeId;
    @Getter
    private final Integer departmentId;

    public SecurityUser(Account account) {
        this.account = account;
        this.employeeId = account.getEmployee().getId();
        this.departmentId = account.getEmployee().getDepartment().getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(account.getRole()));
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return !"FIRED".equalsIgnoreCase(account.getEmployee().getEmploymentStatus());
    }
}
