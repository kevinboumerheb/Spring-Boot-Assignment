package com.springbootassignment.repository;

import com.springbootassignment.model.CustomerModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerModel, Long> {
    List<CustomerModel> findByEmail(String email);
}
