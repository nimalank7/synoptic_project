package uk.first_catering.kiosk.security.model;

import uk.first_catering.kiosk.employee.model.Employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="role")
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private int role_id;

    @Column(name = "type")
    private String type;

    @ManyToMany
    List<Employee> employees;

    public Role() {
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
