package uk.first_catering.kiosk.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.first_catering.kiosk.ExceptionHandler.CardTimedOutException;
import uk.first_catering.kiosk.card.model.Card;
import uk.first_catering.kiosk.card.service.CardService;
import uk.first_catering.kiosk.employee.model.Employee;
import uk.first_catering.kiosk.employee.model.EmployeeResponse;
import uk.first_catering.kiosk.employee.service.EmployeeService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/employee")
public class EmployeeResource {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    CardService cardService;

    @RequestMapping(path="", method=RequestMethod.POST)
    public ResponseEntity<?> registerEmployee(
            @CookieValue(name="card_id", required=false) String card_id,
            @RequestBody Employee employee,
            HttpServletResponse response
    ) {
        if(card_id == null) {
            throw new CardTimedOutException("You were timed out. Please tap in again.");
        }

        Card card = cardService.findById(card_id);
        EmployeeResponse employeeResponse = employeeService.registerEmployee(card, employee);
        Cookie cookie = new Cookie("card_id", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().body(employeeResponse);
    }

    @RequestMapping(path="", method=RequestMethod.PATCH)
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @CookieValue(name="card_id", required=false) String card_id,
            @RequestBody Employee employee
    ) {

        if(card_id == null) {
            throw new CardTimedOutException("You were timed out. Please tap in again.");
        }

        return ResponseEntity.ok().body(employeeService.updateEmployee(employee));
    }
}
