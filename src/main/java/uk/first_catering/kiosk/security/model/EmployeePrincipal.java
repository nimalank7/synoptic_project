package uk.first_catering.kiosk.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uk.first_catering.kiosk.employee.model.Employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EmployeePrincipal implements UserDetails {

    private Employee employee;

    public EmployeePrincipal(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        getEmployee().getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getType())));

        return authorities;
    }

    @Override
    public String getPassword() {
        return getEmployee().getPin();
    }

    @Override
    public String getUsername() {
        return getEmployee().getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
