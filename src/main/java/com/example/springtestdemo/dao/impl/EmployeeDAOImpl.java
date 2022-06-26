package com.example.springtestdemo.dao.impl;

import com.example.springtestdemo.dao.AbstractHibernateDAO;
import com.example.springtestdemo.dao.EmployeeDAO;
import com.example.springtestdemo.domain.Employee;
import com.example.springtestdemo.exceptions.DataNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeDAOImpl extends AbstractHibernateDAO<Employee> implements EmployeeDAO {

    public EmployeeDAOImpl() {
        setClazz(Employee.class);
    }

    @Override
    public Employee getEmployeeById(Integer id) {
        return findById(id);
    }

    @Override
    public Integer addEmployee(Employee employee) {
        return add(employee);
    }

    @Override
    public Boolean checkExistingEmail(String email) {
        Query query = getCurrentSession().createQuery("from Employee e where e.email=:email");
        query.setParameter("email", email);
        List<Employee> emps = query.getResultList();

        return !emps.isEmpty();
    }

    @Override
    public Optional<Employee> getEmployeeWithProjects(Integer id) {
        Query query = getCurrentSession().createQuery("from Employee e left join fetch e.projects where e.id=:id");
        query.setParameter("id", id);
        List<Employee> emps = query.getResultList();

        return emps.isEmpty() ? Optional.empty() : Optional.of(emps.get(0));
    }
}
