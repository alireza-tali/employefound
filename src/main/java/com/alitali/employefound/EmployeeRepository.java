package com.alitali.employefound;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByEmployed(Boolean employed);
}
