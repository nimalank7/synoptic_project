package uk.first_catering.kiosk.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.first_catering.kiosk.card.model.Card;
import uk.first_catering.kiosk.employee.model.Employee;
import uk.first_catering.kiosk.employee.repository.EmployeeRepository;

@RestController
@RequestMapping("/employee")
public class EmployeeResource {

    @Autowired
    EmployeeRepository employeeRepository;

    @RequestMapping(path="{employeeName}", method= RequestMethod.GET)
    public Card getAssociatedCard(@PathVariable String employeeName) {
        Employee employee = employeeRepository.findEmployeeByName(employeeName);
        return employee.getCard();
    }
}
