package uk.first_catering.kiosk.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uk.first_catering.kiosk.employee.model.Employee;
import uk.first_catering.kiosk.employee.repository.EmployeeRepository;
import uk.first_catering.kiosk.security.model.EmployeePrincipal;

@Service
public class EmployeePrincipalService implements UserDetailsService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findEmployeeByName(s);
        return new EmployeePrincipal(employee);
    }
}
