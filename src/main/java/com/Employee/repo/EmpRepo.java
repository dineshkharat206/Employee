package com.Employee.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Employee.model.Employee;


@Repository
public interface EmpRepo extends CrudRepository<Employee, Integer>{

}
