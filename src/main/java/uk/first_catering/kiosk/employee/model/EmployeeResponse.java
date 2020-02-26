package uk.first_catering.kiosk.employee.model;

public class EmployeeResponse {

    String message;

    Employee employee;

    public EmployeeResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
