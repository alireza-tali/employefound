package com.alitali.employefound;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {
    private final EmployeeRepository repository;
    private final EmployeeResourceAssembler assembler;

    public EmployeeController(EmployeeRepository repository, EmployeeResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {

        List<EntityModel<Employee>> employees = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }
    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
    }
    @GetMapping("/employees/employed")
    CollectionModel<EntityModel<Employee>> allEmployed() {

        List<EntityModel<Employee>> employees = repository.findByEmployed(true).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }
    @GetMapping("/employees/notemployed")
    CollectionModel<EntityModel<Employee>> notEmployed() {

        List<EntityModel<Employee>> employees = repository.findByEmployed(false).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }
    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) throws URISyntaxException {
        newEmployee.setEmployed(false);
        EntityModel<Employee> resource = assembler.toModel(repository.save(newEmployee));

        return ResponseEntity
                .created(resource.getLink("self").get().toUri())
                .body(resource);
    }

    @PutMapping("/employees/{id}/employed")
    ResponseEntity<?> foundEmployee(@RequestBody Salary salary, @PathVariable Long id) throws URISyntaxException {
        Employee updatedEmployee = repository.findById(id)
                .map(employee -> {
                    employee.setSalary(salary.getSalary());
                    employee.setEmployed(true);
                    return repository.save(employee);
                })
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        EntityModel<Employee> resource = assembler.toModel(updatedEmployee);
        return ResponseEntity
                .created(resource.getLink("self").get().toUri())
                .body(resource);
    }
    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) throws URISyntaxException {
        Employee updatedEmployee = repository.findById(id)
                .map(employee -> {
                    employee.setFirstName(newEmployee.getFirstName());
                    employee.setLastName(newEmployee.getLastName());
                    employee.setEmployed(newEmployee.getEmployed());
                    employee.setSalary(newEmployee.getSalary());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    newEmployee.setEmployed(false);
                    return repository.save(newEmployee);
                });

        EntityModel<Employee> resource = assembler.toModel(updatedEmployee);
        return ResponseEntity
                .created(resource.getLink("self").get().toUri())
                .body(resource);
    }
    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
