package uk.first_catering.kiosk.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.first_catering.kiosk.card.model.Card;
import uk.first_catering.kiosk.employee.model.Employee;
import uk.first_catering.kiosk.employee.model.EmployeeResponse;
import uk.first_catering.kiosk.employee.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public EmployeeResponse registerEmployee(Card card, Employee employee) {
        employee.setCard(card);
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setMessage("Your card has been registered.");
        employeeResponse.setEmployee(employeeRepository.save(employee));

        return employeeResponse;
    }

    public EmployeeResponse updateEmployee(Employee employee) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setEmployee(employeeRepository.save(employee));
        employeeResponse.setMessage("Your details have been updated");
        return employeeResponse;


    }
}
