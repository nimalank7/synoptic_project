package uk.first_catering.kiosk.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.first_catering.kiosk.employee.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @Query(value ="SELECT * FROM Employee WHERE name=:name", nativeQuery = true)
    Employee findEmployeeByName(@Param("name") String name);
}
