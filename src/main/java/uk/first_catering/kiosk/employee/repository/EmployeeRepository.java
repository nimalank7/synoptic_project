package uk.first_catering.kiosk.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.first_catering.kiosk.employee.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

}
