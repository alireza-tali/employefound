package com.alitali.employefound;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;

@Entity
@Data
@Table(name = "EMPLOYEE_TABLE")
public class Employee extends RepresentationModel {
    private @Id
    @GeneratedValue
    Long id;
    private String firstName;
    private String lastName;
    private Boolean employed = false;
    private Long salary = 0L;

    public Employee() {
    }

    public Employee(String firstName, String lastName, Boolean employed, Long salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employed = employed;
        this.salary = salary;
    }
}
